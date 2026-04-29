package com.vgleadsheets.bottombar

import androidx.lifecycle.viewModelScope
import com.vgleadsheets.analytics.Analytics
import com.vgleadsheets.appcomm.EventDispatcher
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.common.debug.ShowDebugProvider
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.list.DelayManager
import net.sigmabeta.sage.logging.Hatchet
import com.vgleadsheets.viewmodel.VglsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class NavBarViewModel @Inject constructor(
    override val dispatchers: VglsDispatchers,
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
