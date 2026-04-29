package com.vgleadsheets.scaffold.systemui

import androidx.lifecycle.viewModelScope
import com.vgleadsheets.analytics.Analytics
import com.vgleadsheets.appcomm.EventDispatcher
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.common.debug.ShowDebugProvider
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.list.DelayManager
import net.sigmabeta.sage.logging.Hatchet
import com.vgleadsheets.nav.SystemUiVisibility
import com.vgleadsheets.viewmodel.VglsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class SystemUiViewModel@Inject constructor(
    override val analytics: Analytics,
    override val dispatchers: VglsDispatchers,
    override val delayManager: DelayManager,
    override val hatchet: Hatchet,
    override val eventDispatcher: EventDispatcher,
    override val showDebugProvider: ShowDebugProvider,
) : VglsViewModel<SystemUiState>() {
    override val screenIdentifier = null

    override fun initialState() = SystemUiState()

    override fun sendInitAction() = Unit

    init {
        eventDispatcher.addEventSink(this)
    }

    override fun sendAction(action: VglsAction) = handleAction(action)

    override fun sendEvent(event: VglsEvent) = handleEvent(event)

    override fun handleAction(action: VglsAction) = Unit

    override fun handleEvent(event: VglsEvent) {
        viewModelScope.launch(scheduler.dispatchers.main) {
            hatchet.v("${this@SystemUiViewModel.javaClass.simpleName} - Handling event: $event")
            when (event) {
                is VglsEvent.NavigateSuccessTo -> updateCurrentDestination(event.destination)
                is VglsEvent.HideUiChrome -> hideSystemUi()
                is VglsEvent.ShowUiChrome -> showSystemUi()
            }
        }
    }

    private fun updateCurrentDestination(destination: String) = updateState {
        it.copy(currentDestination = destination)
    }

    private fun showSystemUi() {
        if (internalUiState.value.visibility == SystemUiVisibility.VISIBLE) {
            return
        }

        updateState {
            it.copy(visibility = SystemUiVisibility.VISIBLE)
        }
    }

    private fun hideSystemUi() {
        if (internalUiState.value.visibility == SystemUiVisibility.HIDDEN) {
            return
        }

        updateState {
            it.copy(visibility = SystemUiVisibility.HIDDEN)
        }
    }
}
