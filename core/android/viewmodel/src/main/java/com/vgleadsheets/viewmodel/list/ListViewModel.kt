package com.vgleadsheets.viewmodel.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.EventDispatcher
import com.vgleadsheets.appcomm.EventSink
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.common.debug.ShowDebugProvider
import com.vgleadsheets.list.BrainProvider
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.nav.Destination
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ListViewModel @AssistedInject constructor(
    brainProvider: BrainProvider,
    private val hatchet: Hatchet,
    private val eventDispatcher: EventDispatcher,
    private val showDebugProvider: ShowDebugProvider,
    @Assisted destination: Destination,
    @Assisted idArg: Long,
    @Assisted stringArg: String?,
) : ViewModel(),
    ActionSink,
    EventSink {
    private val brain: ListViewModelBrain = brainProvider.provideBrain(
        destination,
        viewModelScope,
    )

    val uiState = brain.uiStateActual

    val showDebug = showDebugProvider.showDebugFlow

    init {

        val initAction = when {
            (idArg > 0L) -> VglsAction.InitWithId(idArg)
            stringArg != null -> VglsAction.InitWithString(stringArg)
            else -> VglsAction.InitNoArgs
        }

        this.sendAction(initAction)

        brain.uiEvents
            .onEach { eventDispatcher.sendEvent(it) }
            .launchIn(viewModelScope)
    }

    fun onResume() {
        eventDispatcher.addEventSink(this)
    }

    fun onPause() {
        eventDispatcher.removeEventSink(this)
    }

    override fun sendAction(action: VglsAction) {
        when (action) {
            is VglsAction.Resume -> onResume()
            is VglsAction.Pause -> onPause()
            else -> {}
        }
        brain.sendAction(action)
    }

    override fun sendEvent(event: VglsEvent) = brain.sendEvent(event)
}
