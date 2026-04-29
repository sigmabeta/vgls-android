package com.vgleadsheets.ui.viewer

import androidx.lifecycle.viewModelScope
import com.vgleadsheets.analytics.Analytics
import com.vgleadsheets.analytics.AnalyticsScreen
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.EventDispatcher
import com.vgleadsheets.appcomm.EventSink
import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.common.debug.ShowDebugProvider
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.list.DelayManager
import net.sigmabeta.sage.logging.Hatchet
import com.vgleadsheets.repository.SongRepository
import com.vgleadsheets.repository.history.SongHistoryRepository
import com.vgleadsheets.settings.GeneralSettingsManager
import com.vgleadsheets.ui.StringProvider
import com.vgleadsheets.urlinfo.UrlInfoProvider
import com.vgleadsheets.viewmodel.VglsViewModel
import net.sigmabeta.sage.wakelocks.WakeLockManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ViewerViewModel @AssistedInject constructor(
    private val stringProvider: StringProvider,
    private val songRepository: SongRepository,
    private val songHistoryRepository: SongHistoryRepository,
    private val urlInfoProvider: UrlInfoProvider,
    private val generalSettingsManager: GeneralSettingsManager,
    private val wakeLockManager: WakeLockManager,
    override val hatchet: Hatchet,
    override val analytics: Analytics,
    override val dispatchers: VglsDispatchers,
    override val delayManager: DelayManager,
    override val eventDispatcher: EventDispatcher,
    override val showDebugProvider: ShowDebugProvider,
    @Assisted("id") val idArg: Long,
    @Assisted("page") val pageArg: Long,
) : VglsViewModel<ViewerState>(),
    ActionSink,
    EventSink {

    private var chromeVisibilityTimer: Job? = null
    private var buttonVisibilityTimer: Job? = null
    private var historyTimer: Job? = null

    override val screenIdentifier = AnalyticsScreen.SHEET_VIEWER

    init {
        viewModelScope.launch(dispatchers.main) {
            sendInitAction()
        }
    }

    override fun initialState() = ViewerState()

    override fun sendInitAction() {
        val initAction = VglsAction.InitWithPageNumber(
            idArg,
            pageArg
        )

        this.sendAction(initAction)
    }

    override fun handleAction(action: VglsAction) {
        hatchet.d("${this.javaClass.simpleName} - Handling action: $action")
        when (action) {
            is VglsAction.Resume -> resume()
            is VglsAction.Pause -> pause()
            is VglsAction.AppBack -> onBackPressed()
            is VglsAction.DeviceBack -> onBackPressed()
            is VglsAction.InitWithPageNumber -> startLoading(action.id, action.pageNumber)
            is VglsAction.PageClicked -> maybeShowUi()
            is VglsAction.PageZoomedIn -> enableZoom()
            is VglsAction.PageZoomedOutMax -> disableZoom()
            is Action.ScreenClicked -> maybeShowUi()
            is Action.PrevButtonClicked, Action.NextButtonClicked -> onButtonClicked()
            is Action.LeftArrowPressed, Action.RightArrowPressed -> onButtonClicked()
        }
    }

    override fun handleEvent(event: VglsEvent) {
        hatchet.d("${this.javaClass.simpleName} - Handling event: $event")
        when (event) {
            is VglsEvent.SystemBarsBecameHidden -> startHideButtonsTimer()

            is VglsEvent.SystemBarsBecameShown -> {
                startHideChromeTimer()
                showButtons()
            }
        }
    }

    override fun onCleared() {
        stopHistoryTimer()
    }

    private fun startLoading(id: Long, pageNumber: Long) {
        hatchet.v("Loading song with id $id starting from pagenumber $pageNumber")
        fetchSong(id, pageNumber)
        fetchUrlInfo()
        checkAltSelectionStatus(id)
        checkScreenOnSetting()
        startScreenOnManagement()
    }

    private fun resume() {
        eventDispatcher.addEventSink(this)
        updateTitle()
        startTimers()
    }

    private fun pause() {
        stopTimers()
        eventDispatcher.removeEventSink(this)
    }

    private fun onBackPressed() {
        stopTimers()
    }

    private fun updateTitle() {
        val state = internalUiState.value
        val titleModel = state.title(stringProvider)

        if (titleModel.title != null) {
            emitEvent(
                VglsEvent.UpdateTitle(
                    title = titleModel.title,
                    subtitle = titleModel.subtitle,
                    shouldShowBack = titleModel.shouldShowBack,
                    source = "Viewer",
                )
            )
        }
    }

    private fun fetchSong(id: Long, pageNumber: Long) {
        val operationName = "fetchSong"
        songRepository
            .getSong(id)
            .onEach { song ->
                updateState {
                    it.copy(
                        song = LCE.Content(song),
                        initialPage = pageNumber.toInt(),
                        isSongHistoryEntryRecorded = false,
                    )
                }
            }
            .catch { error ->
                updateState {
                    it.copy(
                        song = LCE.Error(operationName, error)
                    )
                }
            }
            .flowOn(scheduler.dispatchers.disk)
            .launchIn(viewModelScope)
    }

    private fun fetchUrlInfo() {
        urlInfoProvider
            .urlInfoFlow
            .onEach { urlInfo ->
                updateState {
                    it.copy(partApiId = urlInfo.partId)
                }
            }
            .runInBackground(shouldDelay = false)
    }

    private fun checkAltSelectionStatus(id: Long) {
        updateIsAltSelected(LCE.Loading(LOAD_OPERATION_IS_ALT_SELECTED))
        songRepository
            .isAlternateSelected(id)
            .onEach { isAltSelected -> updateIsAltSelected(LCE.Content(isAltSelected)) }
            .catch { updateIsAltSelected(LCE.Error(LOAD_OPERATION_IS_ALT_SELECTED, it)) }
            .runInBackground()
    }

    private fun checkScreenOnSetting() {
        generalSettingsManager
            .getKeepScreenOn()
            .onEach { keepScreenOn -> updateKeepScreencOn(keepScreenOn) }
            .flowOn(scheduler.dispatchers.disk)
            .launchIn(viewModelScope)
    }

    private fun startScreenOnManagement() {
        internalUiState
            .mapNotNull { it.keepScreenOn }
            .distinctUntilChanged()
            .onEach { updateWakeLockManager(it) }
            .flowOn(scheduler.dispatchers.computation)
            .launchIn(viewModelScope)
    }

    private fun updateWakeLockManager(keepScreenOn: Boolean?) {
        keepScreenOn ?: return

        if (keepScreenOn) {
            wakeLockManager.keepScreenOn()
        } else {
            wakeLockManager.allowScreenOff()
        }
    }

    private fun updateIsAltSelected(isAltSelected: LCE<Boolean>) {
        updateState {
            it.copy(isAltSelected = isAltSelected)
        }
    }

    private fun updateKeepScreencOn(keepScreenOn: Boolean) {
        updateState {
            it.copy(keepScreenOn = keepScreenOn)
        }
    }

    private fun maybeShowUi() {
        maybeRestartScreenOnTimer()
        emitEvent(VglsEvent.ShowUiChrome)
    }

    private fun onButtonClicked() {
        maybeRestartScreenOnTimer()
        showButtons()
        startHideButtonsTimer()
    }

    private fun showButtons() {
        updateState {
            it.copy(buttonsVisible = true)
        }
    }

    private fun startTimers() {
        startHideChromeTimer()
        startRecordSongHistoryEntryTimerMaybe()
    }

    private fun maybeRestartScreenOnTimer() {
        if (internalUiState.value.keepScreenOn == true) {
            wakeLockManager.keepScreenOn()
        }
    }

    private fun startHideChromeTimer() {
        chromeVisibilityTimer?.cancel()
        chromeVisibilityTimer = viewModelScope.launch(scheduler.dispatchers.computation) {
            hatchet.v("Starting timer: Hiding UI chrome in $DURATION_CHROME_VISIBILITY ms.")
            delay(DURATION_CHROME_VISIBILITY)

            if (!coroutineContext.isActive) {
                hatchet.w("Timer cancelled: Hide chrome")
                return@launch
            }

            emitEvent(VglsEvent.HideUiChrome)
            chromeVisibilityTimer = null
        }
    }

    private fun startHideButtonsTimer() {
        buttonVisibilityTimer?.cancel()
        buttonVisibilityTimer = viewModelScope.launch(scheduler.dispatchers.computation) {
            hatchet.v("Starting timer: Hiding buttons in $DURATION_BUTTON_VISIBILITY ms.")
            delay(DURATION_BUTTON_VISIBILITY)

            if (!coroutineContext.isActive) {
                hatchet.w("Timer cancelled: Hide buttons")
                return@launch
            }

            updateState {
                it.copy(buttonsVisible = false)
            }
            buttonVisibilityTimer = null
        }
    }

    private fun startRecordSongHistoryEntryTimerMaybe() {
        historyTimer?.cancel()
        historyTimer = internalUiState
            .filter { it.song is LCE.Content && !it.isSongHistoryEntryRecorded }
            .take(1)
            .onEach { state ->
                if (state.song is LCE.Content) {
                    hatchet.v("Starting timer: Recording song history entry in $DURATION_HISTORY_RECORD ms..")
                    delay(DURATION_HISTORY_RECORD)

                    if (historyTimer?.isActive == false) {
                        hatchet.w("Timer cancelled: Record song history entry")
                        return@onEach
                    }

                    hatchet.d("Recording song history entry for ${state.song.data.name}.")

                    songHistoryRepository.recordSongPlay(state.song.data, System.currentTimeMillis())
                    updateState {
                        it.copy(isSongHistoryEntryRecorded = true)
                    }
                    historyTimer = null
                }
            }
            .flowOn(dispatchers.disk)
            .launchIn(viewModelScope)
    }

    private fun stopTimers() {
        stopHideChromeTimer()
        stopHideButtonsTimer()
        stopHistoryTimer()
        wakeLockManager.allowScreenOff()
    }

    private fun stopHideChromeTimer() {
        if (chromeVisibilityTimer != null) {
            hatchet.i("Stopping timer: Hide Chrome.")
            chromeVisibilityTimer?.cancel()
            chromeVisibilityTimer = null
        }
    }

    private fun stopHideButtonsTimer() {
        if (buttonVisibilityTimer != null) {
            hatchet.i("Stopping timer: Hide Buttons.")
            buttonVisibilityTimer?.cancel()
            buttonVisibilityTimer = null
        }
    }

    private fun stopHistoryTimer() {
        if (historyTimer != null) {
            hatchet.i("Stopping timer: Song History Entry.")
            historyTimer?.cancel()
            historyTimer = null
        }
    }

    private fun enableZoom() {
        updateState { it.copy(isZoomedIn = true) }
        maybeRestartScreenOnTimer()
    }

    private fun disableZoom() {
        updateState { it.copy(isZoomedIn = false) }

        maybeRestartScreenOnTimer()
        showButtons()
        startHideButtonsTimer()
    }

    companion object {
        private const val DURATION_BUTTON_VISIBILITY = 1_000L
        private const val DURATION_CHROME_VISIBILITY = 1_500L
        private const val DURATION_HISTORY_RECORD = 10_000L

        internal const val LOAD_OPERATION_IS_ALT_SELECTED = "songs.detail.alternate"
    }
}
