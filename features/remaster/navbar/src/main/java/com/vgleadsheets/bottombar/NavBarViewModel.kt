package com.vgleadsheets.bottombar

import androidx.lifecycle.viewModelScope
import com.vgleadsheets.appcomm.EventDispatcher
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.common.debug.ShowDebugProvider
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.list.DelayManager
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.viewmodel.VglsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class NavBarViewModel @Inject constructor(
    override val dispatchers: VglsDispatchers,
    override val delayManager: DelayManager,
    override val hatchet: Hatchet,
    override val eventDispatcher: EventDispatcher,
    override val showDebugProvider: ShowDebugProvider,
) : VglsViewModel<NavBarState>() {
    init {
        eventDispatcher.addEventSink(this)
    }

    override fun initialState() = NavBarState()

    override fun handleAction(action: VglsAction) {
        viewModelScope.launch(scheduler.dispatchers.main) {
            hatchet.d("${this.javaClass.simpleName} - Handling action: $action")
        }
    }

    override fun handleEvent(event: VglsEvent) {
        viewModelScope.launch(scheduler.dispatchers.main) {
            hatchet.d("${this@NavBarViewModel.javaClass.simpleName} - Handling event: $event")
            when (event) {
                is VglsEvent.HideUiChrome -> hideBottomBar()
                is VglsEvent.ShowUiChrome -> showBottomBar()
            }
        }
    }

    private fun showBottomBar() {
        hatchet.d("Showing bottom bar.")
        viewModelScope.launch {
            updateState {
                it.copy(visibility = NavBarVisibility.VISIBLE)
            }
        }
    }

    private fun hideBottomBar() {
        hatchet.d("Hiding bottom bar.")
        updateState {
            it.copy(visibility = NavBarVisibility.HIDDEN)
        }
        emitEvent(VglsEvent.UiChromeBecameHidden)
    }
}
