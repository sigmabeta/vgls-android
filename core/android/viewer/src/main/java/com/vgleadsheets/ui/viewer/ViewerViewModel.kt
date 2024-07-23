package com.vgleadsheets.ui.viewer

import androidx.lifecycle.viewModelScope
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.EventDispatcher
import com.vgleadsheets.appcomm.EventSink
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.model.Song
import com.vgleadsheets.repository.VglsRepository
import com.vgleadsheets.repository.history.SongHistoryRepository
import com.vgleadsheets.ui.StringProvider
import com.vgleadsheets.urlinfo.UrlInfoProvider
import com.vgleadsheets.viewmodel.VglsViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ViewerViewModel @AssistedInject constructor(
    override val hatchet: Hatchet,
    private val stringProvider: StringProvider,
    private val repository: VglsRepository,
    private val songHistoryRepository: SongHistoryRepository,
    private val urlInfoProvider: UrlInfoProvider,
    override val dispatchers: VglsDispatchers,
    override val eventDispatcher: EventDispatcher,
    @Assisted("id") idArg: Long,
    @Assisted("page") pageArg: Long,
) : VglsViewModel<ViewerState>(),
    ActionSink,
    EventSink {
    init {
        eventDispatcher.addEventSink(this)

        val initAction = Action.InitWithPageNumber(
            idArg,
            pageArg
        )

        this.sendAction(initAction)
        emitEvent(VglsEvent.ShowUiChrome)
        hideChromeSoon()
    }

    private var chromeVisibilityTimer: Job? = null
    private var buttonVisibilityTimer: Job? = null
    private var historyTimer: Job? = null

    override fun initialState() = ViewerState()

    override fun handleAction(action: VglsAction) {
        hatchet.d("${this.javaClass.simpleName} - Handling action: $action")
        when (action) {
            is VglsAction.Resume -> resume()
            is Action.InitWithPageNumber -> startLoading(action.id, action.pageNumber)
            is Action.PageClicked -> emitEvent(VglsEvent.ShowUiChrome)
            is Action.PrevButtonClicked, Action.NextButtonClicked -> onButtonClicked()
            is Action.SongLoaded -> startHistoryTimer(action.song)
        }
    }

    override fun handleEvent(event: VglsEvent) {
        hatchet.d("${this.javaClass.simpleName} - Handling event: $event")
        when (event) {
            is VglsEvent.UiChromeBecameHidden -> hideButtonsSoon()
            is VglsEvent.UiChromeBecameShown -> {
                hideChromeSoon()
                showButtons()
            }
        }
    }

    override fun onCleared() {
        eventDispatcher.removeEventSink(this)
        stopHistoryTimer()
    }

    private fun startLoading(id: Long, pageNumber: Long) {
        fetchSong(id, pageNumber)
        fetchUrlInfo()
    }

    private fun resume() {
        updateTitle()
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
        repository
            .getSong(id)
            .onEach { song ->
                sendAction(Action.SongLoaded(song))
                updateState {
                    it.copy(
                        song = song,
                        initialPage = pageNumber.toInt(),
                    )
                }
            }
            .flowOn(dispatchers.disk)
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
            .flowOn(dispatchers.disk)
            .launchIn(viewModelScope)
    }

    private fun hideChromeSoon() {
        chromeVisibilityTimer?.cancel()
        chromeVisibilityTimer = viewModelScope.launch(dispatchers.computation) {
            hatchet.v("Hiding UI chrome in $DURATION_CHROME_VISIBILITY ms.")
            delay(DURATION_CHROME_VISIBILITY)
            emitEvent(VglsEvent.HideUiChrome)
        }
    }

    private fun onButtonClicked() {
        showButtons()
        hideButtonsSoon()
    }

    private fun showButtons() {
        updateState {
            it.copy(buttonsVisible = true)
        }
    }

    private fun hideButtonsSoon() {
        buttonVisibilityTimer?.cancel()
        buttonVisibilityTimer = viewModelScope.launch {
            delay(DURATION_BUTTON_VISIBILITY)
            updateState {
                it.copy(buttonsVisible = false)
            }
        }
    }

    private fun startHistoryTimer(song: Song) {
        historyTimer = viewModelScope.launch {
            delay(DURATION_HISTORY_RECORD)
            hatchet.d("This song has officially been viewed.")

            songHistoryRepository.recordSongPlay(song)
            historyTimer = null
        }
    }

    private fun stopHistoryTimer() {
        if (historyTimer != null) {
            hatchet.i("This song was not officially viewed.")
            historyTimer?.cancel()
            historyTimer = null
        }
    }

    companion object {
        private const val DURATION_BUTTON_VISIBILITY = 1_500L
        private const val DURATION_CHROME_VISIBILITY = 3_000L
        private const val DURATION_HISTORY_RECORD = 1_000L
    }
}
