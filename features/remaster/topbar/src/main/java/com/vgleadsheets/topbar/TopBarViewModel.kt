package com.vgleadsheets.topbar

import androidx.lifecycle.viewModelScope
import com.vgleadsheets.appcomm.EventDispatcher
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.settings.part.SelectedPartManager
import com.vgleadsheets.viewmodel.VglsViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TopBarViewModel @AssistedInject constructor(
    private val selectedPartManager: SelectedPartManager,
    private val dispatchers: VglsDispatchers,
    private val coroutineScope: CoroutineScope,
    private val hatchet: Hatchet,
    @Assisted private val eventDispatcher: EventDispatcher,
) : VglsViewModel<TopBarState>() {
    override fun initialState() = TopBarState()

    init {
        eventDispatcher.addEventSink(this)
        loadSelectedPart()

        uiEvents
            .onEach {
                hatchet.d("Sending event: $it")
                eventDispatcher.sendEvent(it)
            }
            .launchIn(viewModelScope)
    }

    override fun sendAction(action: VglsAction) = handleAction(action)

    override fun sendEvent(event: VglsEvent) = handleEvent(event)

    private fun updateTitle(title: TitleBarModel) {
        hatchet.v("Updating title: $title")
        internalUiState.update {
            it.copy(model = title)
        }
    }

    private fun loadSelectedPart() {
        selectedPartManager
            .selectedPartFlow()
            .onEach { selectedPart ->
                internalUiState.update {
                    it.copy(selectedPart = selectedPart.apiId)
                }
            }
            .flowOn(dispatchers.disk)
            .launchIn(coroutineScope)
    }

    override fun onCleared() {
        eventDispatcher.removeEventSink(this)
    }

    private fun handleAction(action: VglsAction) {
        coroutineScope.launch(dispatchers.main) {
            hatchet.d("${this.javaClass.simpleName} - Handling action: $action")
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

                is TopBarAction.AppBack

                -> eventDispatcher.sendEvent(
                    VglsEvent.NavigateBack(
                        "TopBar"
                    )
                )
            }
        }
    }

    private fun handleEvent(event: VglsEvent) {
        coroutineScope.launch(dispatchers.main) {
            hatchet.d("${this@TopBarViewModel.javaClass.simpleName} - Handling event: $event")
            when (event) {
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
}
