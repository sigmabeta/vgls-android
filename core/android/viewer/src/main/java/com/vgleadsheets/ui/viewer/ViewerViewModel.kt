package com.vgleadsheets.ui.viewer

import androidx.lifecycle.viewModelScope
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.EventDispatcher
import com.vgleadsheets.appcomm.EventSink
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.repository.VglsRepository
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewerViewModel @AssistedInject constructor(
    override val hatchet: Hatchet,
    private val stringProvider: StringProvider,
    private val repository: VglsRepository,
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

    override fun initialState() = ViewerState()

    override fun handleAction(action: VglsAction) {
        hatchet.d("${this.javaClass.simpleName} - Handling action: $action")
        when (action) {
            is VglsAction.Resume -> resume()
            is Action.InitWithPageNumber -> startLoading(action.id, action.pageNumber)
            is Action.PageClicked -> emitEvent(VglsEvent.ShowUiChrome)
            is Action.PrevButtonClicked, Action.NextButtonClicked -> onButtonClicked()
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
    }

    private fun startLoading(id: Long, pageNumber: Long) {
        fetchSong(id, pageNumber)
        fetchUrlInfo()
    }

    private fun updateState(updater: (ViewerState) -> ViewerState) {
        viewModelScope.launch(dispatchers.main) {
            val oldState = internalUiState.value
            val newState = updater(oldState)

            hatchet.v("Updating state: $newState")

            internalUiState.value = newState
        }
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
        internalUiState.update {
            it.copy(buttonsVisible = true)
        }
    }

    private fun hideButtonsSoon() {
        buttonVisibilityTimer?.cancel()
        buttonVisibilityTimer = viewModelScope.launch {
            delay(1_000L)
            internalUiState.update {
                it.copy(buttonsVisible = false)
            }
        }
    }

    companion object {
        private const val DURATION_CHROME_VISIBILITY = 3_000L
    }
}
