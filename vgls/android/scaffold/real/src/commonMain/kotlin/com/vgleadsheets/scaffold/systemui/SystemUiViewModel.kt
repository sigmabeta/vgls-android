package com.vgleadsheets.scaffold.systemui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vgleadsheets.nav.SystemUiVisibility
import com.vgleadsheets.viewmodel.VglsViewModel
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.launch
import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.appcomm.EventDispatcher
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.appcomm.SageEvent
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.debug.ShowDebugProvider
import net.sigmabeta.sage.di.AppScope
import net.sigmabeta.sage.list.DelayManager
import net.sigmabeta.sage.logging.Hatchet

@ContributesIntoMap(AppScope::class, binding = binding<ViewModel>())
@ViewModelKey
class SystemUiViewModel @Inject constructor(
    override val analytics: Analytics,
    override val dispatchers: SageDispatchers,
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

    override fun sendAction(action: SageAction) = handleAction(action)

    override fun sendEvent(event: SageEvent) = handleEvent(event)

    override fun handleAction(action: SageAction) = Unit

    override fun handleEvent(event: SageEvent) {
        viewModelScope.launch(scheduler.dispatchers.main) {
            hatchet.v("${this@SystemUiViewModel.javaClass.simpleName} - Handling event: $event")
            when (event) {
                is SageEvent.NavigateSuccessTo -> updateCurrentDestination(event.destination)
                is SageEvent.HideUiChrome -> hideSystemUi()
                is SageEvent.ShowUiChrome -> showSystemUi()
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
