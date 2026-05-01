package com.vgleadsheets.topbar

import androidx.lifecycle.viewModelScope
import com.vgleadsheets.settings.part.SelectedPartManager
import com.vgleadsheets.viewmodel.VglsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.appcomm.EventDispatcher
import net.sigmabeta.sage.appcomm.VglsAction
import net.sigmabeta.sage.appcomm.VglsEvent
import net.sigmabeta.sage.components.TitleBarModel
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.debug.ShowDebugProvider
import net.sigmabeta.sage.list.DelayManager
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.nav.Destination
import javax.inject.Inject

@HiltViewModel
class TopBarViewModel @Inject constructor(
    private val selectedPartManager: SelectedPartManager,
    override val analytics: Analytics,
    override val dispatchers: SageDispatchers,
    override val delayManager: DelayManager,
    override val hatchet: Hatchet,
    override val eventDispatcher: EventDispatcher,
    override val showDebugProvider: ShowDebugProvider,
) : VglsViewModel<TopBarState>() {
    override val screenIdentifier = null

    override fun initialState() = TopBarState()

    override fun sendInitAction() = Unit

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
                is VglsEvent.NavigateSuccessTo -> updateCurrentDestination(event.destination)

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

    private fun updateCurrentDestination(destination: String) = updateState {
        it.copy(currentDestination = destination)
    }

    private fun showTopBar() {
        if (internalUiState.value.visibility == TopBarVisibility.VISIBLE) {
            return
        }

        updateState {
            it.copy(visibility = TopBarVisibility.VISIBLE)
        }
    }

    private fun hideTopBar() {
        if (internalUiState.value.visibility == TopBarVisibility.HIDDEN) {
            return
        }

        updateState {
            it.copy(visibility = TopBarVisibility.HIDDEN)
        }
    }

    private fun updateTitle(title: TitleBarModel) {
        if (internalUiState.value.model == title) {
            return
        }

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
