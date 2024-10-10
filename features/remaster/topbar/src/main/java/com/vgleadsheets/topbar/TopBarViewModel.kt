package com.vgleadsheets.topbar

import androidx.lifecycle.viewModelScope
import com.vgleadsheets.appcomm.EventDispatcher
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.common.debug.ShowDebugProvider
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.list.DelayManager
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.settings.part.SelectedPartManager
import com.vgleadsheets.viewmodel.VglsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopBarViewModel @Inject constructor(
    private val selectedPartManager: SelectedPartManager,
    override val dispatchers: VglsDispatchers,
    override val delayManager: DelayManager,
    override val hatchet: Hatchet,
    override val eventDispatcher: EventDispatcher,
    override val showDebugProvider: ShowDebugProvider,
) : VglsViewModel<TopBarState>() {
    private var showTopBarJob: Job? = null

    override fun initialState() = TopBarState()

    init {
        eventDispatcher.addEventSink(this)
        loadSelectedPart()
    }

    override fun sendAction(action: VglsAction) = handleAction(action)

    override fun sendEvent(event: VglsEvent) = handleEvent(event)

    override fun handleAction(action: VglsAction) {
        viewModelScope.launch(scheduler.dispatchers.main) {
            hatchet.v("${this.javaClass.simpleName} - Handling action: $action")
            when (action) {
                is TopBarAction.Menu -> eventDispatcher.sendEvent(
                    VglsEvent.NavigateTo(
                        Destination.MENU.noArgs(),
                        "TopBar"
                    )
                )

                is TopBarAction.OpenPartPicker -> eventDispatcher.sendEvent(
                    VglsEvent.NavigateTo(
                        Destination.PART_PICKER.noArgs(),
                        "TopBar"
                    )
                )

                is VglsAction.AppBack -> eventDispatcher.sendEvent(
                    VglsEvent.NavigateBack(
                        "TopBar"
                    )
                )
            }
        }
    }

    override fun handleEvent(event: VglsEvent) {
        viewModelScope.launch(scheduler.dispatchers.main) {
            hatchet.v("${this@TopBarViewModel.javaClass.simpleName} - Handling event: $event")
            when (event) {
                is VglsEvent.HideTopBar -> hideTopBar()
                is VglsEvent.HideUiChrome -> hideTopBar()
                is VglsEvent.ShowUiChrome -> showTopBar()
                is VglsEvent.UpdateTitle -> updateTitle(
                    TitleBarModel(
                        event.title,
                        event.subtitle,
                        event.shouldShowBack
                    )
                )
            }
        }
    }

    private fun showTopBar() {
        hatchet.d("Showing top bar.")
        showTopBarJob = viewModelScope.launch {
            updateState {
                it.copy(visibility = TopBarVisibility.VISIBLE)
            }
        }
        emitEvent(VglsEvent.TopBarBecameShown)
    }

    private fun hideTopBar() {
        hatchet.d("Hiding top bar.")
        showTopBarJob?.cancel()
        updateState {
            it.copy(visibility = TopBarVisibility.HIDDEN)
        }
        emitEvent(VglsEvent.TopBarBecameHidden)
    }

    private fun updateTitle(title: TitleBarModel) {
        hatchet.v("Updating title: $title")
        updateState {
            it.copy(model = title)
        }
    }

    private fun loadSelectedPart() {
        selectedPartManager
            .selectedPartFlow()
            .onEach { selectedPart ->
                updateState {
                    it.copy(selectedPart = selectedPart.apiId)
                }
            }
            .flowOn(scheduler.dispatchers.disk)
            .launchIn(viewModelScope)
    }
}
