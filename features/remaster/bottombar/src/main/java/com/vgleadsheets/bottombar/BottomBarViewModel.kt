package com.vgleadsheets.bottombar

import androidx.lifecycle.viewModelScope
import com.vgleadsheets.appcomm.EventDispatcher
import com.vgleadsheets.appcomm.Hacks
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.list.DelayManager
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.viewmodel.VglsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel
class BottomBarViewModel @Inject constructor(
    override val dispatchers: VglsDispatchers,
    override val delayManager: DelayManager,
    override val hatchet: Hatchet,
    override val eventDispatcher: EventDispatcher,
) : VglsViewModel<BottomBarState>() {
    init {
        eventDispatcher.addEventSink(this)
    }

    override fun initialState() = BottomBarState()

    override fun handleAction(action: VglsAction) {
        viewModelScope.launch(scheduler.dispatchers.main) {
            hatchet.d("${this.javaClass.simpleName} - Handling action: $action")
        }
    }

    override fun handleEvent(event: VglsEvent) {
        viewModelScope.launch(scheduler.dispatchers.main) {
            hatchet.d("${this@BottomBarViewModel.javaClass.simpleName} - Handling event: $event")
            when (event) {
                is VglsEvent.HideUiChrome -> hideBottomBar()
                is VglsEvent.ShowUiChrome -> showBottomBar()
            }
        }
    }

    private fun showBottomBar() {
        hatchet.d("Showing bottom bar.")
        viewModelScope.launch {
            delay(Hacks.UI_VISIBILITY_ANIM_DELAY)
            updateState {
                it.copy(visibility = BottomBarVisibility.VISIBLE)
            }
        }
    }

    private fun hideBottomBar() {
        hatchet.d("Hiding bottom bar.")
        updateState {
            it.copy(visibility = BottomBarVisibility.HIDDEN)
        }
        emitEvent(VglsEvent.UiChromeBecameHidden)
    }
}
