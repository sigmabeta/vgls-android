package com.vgleadsheets.bottombar

import androidx.lifecycle.viewModelScope
import com.vgleadsheets.viewmodel.VglsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.appcomm.EventDispatcher
import net.sigmabeta.sage.appcomm.VglsAction
import net.sigmabeta.sage.appcomm.VglsEvent
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.debug.ShowDebugProvider
import net.sigmabeta.sage.list.DelayManager
import net.sigmabeta.sage.logging.Hatchet
import javax.inject.Inject

@HiltViewModel
class NavBarViewModel @Inject constructor(
    override val dispatchers: SageDispatchers,
    override val analytics: Analytics,
    override val delayManager: DelayManager,
    override val hatchet: Hatchet,
    override val eventDispatcher: EventDispatcher,
    override val showDebugProvider: ShowDebugProvider,
) : VglsViewModel<NavBarState>() {
    init {
        eventDispatcher.addEventSink(this)
    }

    override val screenIdentifier = null

    override fun initialState() = NavBarState()

    override fun sendInitAction() = Unit

    override fun handleAction(action: VglsAction) {
        viewModelScope.launch(scheduler.dispatchers.main) {
            hatchet.v("${this.javaClass.simpleName} - Handling action: $action")
        }
    }

    override fun handleEvent(event: VglsEvent) {
        viewModelScope.launch(scheduler.dispatchers.main) {
            hatchet.v("${this@NavBarViewModel.javaClass.simpleName} - Handling event: $event")
            when (event) {
                is VglsEvent.NavigateSuccessTo -> updateCurrentDestination(event.destination)
                is VglsEvent.HideUiChrome -> hideNavBar()
                is VglsEvent.ShowUiChrome -> showNavBar()
            }
        }
    }

    private fun updateCurrentDestination(destination: String) = updateState {
        it.copy(currentDestination = destination)
    }

    private fun showNavBar() {
        if (internalUiState.value.visibility == NavBarVisibility.VISIBLE) {
            return
        }

        updateState {
            it.copy(visibility = NavBarVisibility.VISIBLE)
        }
    }

    private fun hideNavBar() {
        if (internalUiState.value.visibility == NavBarVisibility.HIDDEN) {
            return
        }

        updateState {
            it.copy(visibility = NavBarVisibility.HIDDEN)
        }
    }
}
