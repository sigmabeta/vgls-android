package com.vgleadsheets.topbar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vgleadsheets.settings.part.SelectedPartManager
import com.vgleadsheets.viewmodel.VglsViewModel
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.appcomm.EventDispatcher
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.appcomm.SageEvent
import net.sigmabeta.sage.components.TitleBarModel
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.debug.ShowDebugProvider
import net.sigmabeta.sage.di.AppScope
import net.sigmabeta.sage.list.DelayManager
import net.sigmabeta.sage.logging.Hatchet
import com.vgleadsheets.nav.Destination

@ContributesIntoMap(AppScope::class, binding = binding<ViewModel>())
@ViewModelKey
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

    override fun sendAction(action: SageAction) = handleAction(action)

    override fun sendEvent(event: SageEvent) = handleEvent(event)

    override fun handleAction(action: SageAction) {
        viewModelScope.launch(scheduler.dispatchers.main) {
            hatchet.v("${this.javaClass.simpleName} - Handling action: $action")
            when (action) {
                is TopBarAction.Menu -> eventDispatcher.sendEvent(
                    SageEvent.NavigateTo(
                        Destination.MENU.noArgs(),
                        "TopBar"
                    )
                )

                is TopBarAction.OpenPartPicker -> eventDispatcher.sendEvent(
                    SageEvent.NavigateTo(
                        Destination.PART_PICKER.noArgs(),
                        "TopBar"
                    )
                )

                is SageAction.AppBack -> eventDispatcher.sendEvent(
                    SageEvent.NavigateBack(
                        "TopBar"
                    )
                )
            }
        }
    }

    override fun handleEvent(event: SageEvent) {
        viewModelScope.launch(scheduler.dispatchers.main) {
            hatchet.v("${this@TopBarViewModel.javaClass.simpleName} - Handling event: $event")
            when (event) {
                is SageEvent.NavigateSuccessTo -> updateCurrentDestination(event.destination)

                is SageEvent.HideTopBar -> hideTopBar()

                is SageEvent.HideUiChrome -> hideTopBar()

                is SageEvent.ShowUiChrome -> showTopBar()

                is SageEvent.UpdateTitle -> updateTitle(
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
