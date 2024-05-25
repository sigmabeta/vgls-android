package com.vgleadsheets.viewmodel.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.EventDispatcher
import com.vgleadsheets.appcomm.EventSink
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
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
    val hatchet: Hatchet,
    @Assisted destination: Destination,
    @Assisted private val eventDispatcher: EventDispatcher,
    @Assisted idArg: Long,
    @Assisted stringArg: String?,
) : ViewModel(),
    ActionSink,
    EventSink {
    private val brain: ListViewModelBrain = brainProvider.provideBrain(
        destination,
        viewModelScope,
    )

    val handlesBack: Boolean = brain.handlesBack

    val uiState = brain.uiStateActual
    private val uiEvents = brain.uiEvents

    init {
        eventDispatcher.addEventSink(this)

        val initAction = when {
            (idArg > 0L) -> VglsAction.InitWithId(idArg)
            stringArg != null -> VglsAction.InitWithString(stringArg)
            else -> VglsAction.InitNoArgs
        }

        this.sendAction(initAction)
        uiEvents
            .onEach {
                hatchet.d("Sending event: $it")
                eventDispatcher.sendEvent(it)
            }
            .launchIn(viewModelScope)
    }

    override fun onCleared() {
        eventDispatcher.removeEventSink(this)
    }

    override fun sendAction(action: VglsAction) = brain.sendAction(action)

    override fun sendEvent(event: VglsEvent) = brain.sendEvent(event)
}
