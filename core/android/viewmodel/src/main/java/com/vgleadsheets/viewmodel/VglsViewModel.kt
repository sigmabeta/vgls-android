package com.vgleadsheets.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.EventDispatcher
import com.vgleadsheets.appcomm.EventSink
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.appcomm.VglsState
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.logging.Hatchet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

abstract class VglsViewModel<StateType : VglsState> :
    ViewModel(),
    ActionSink,
    EventSink {
    protected abstract val hatchet: Hatchet
    protected abstract val dispatchers: VglsDispatchers
    protected abstract val coroutineScope: CoroutineScope
    protected abstract val eventDispatcher: EventDispatcher

    protected val internalUiState = MutableStateFlow(initialState())
    val uiState = internalUiState.asStateFlow()

    protected val internalUiEvents = MutableSharedFlow<VglsEvent>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val uiEventsJob = internalUiEvents
        .onEach { eventDispatcher.sendEvent(it) }
        .launchIn(viewModelScope)

    abstract fun initialState(): StateType

    protected abstract fun handleAction(action: VglsAction)

    protected abstract fun handleEvent(event: VglsEvent)

    override fun sendAction(action: VglsAction) {
        if (action is VglsAction.DeviceBack) {
            emitEvent(VglsEvent.NavigateBack(this.javaClass.simpleName))
            return
        }

        coroutineScope.launch(dispatchers.main) {
            handleAction(action)
        }
    }

    override fun sendEvent(event: VglsEvent) {
        coroutineScope.launch(dispatchers.main) {
            handleEvent(event)
        }
    }

    protected fun emitEvent(event: VglsEvent) {
        coroutineScope.launch(dispatchers.main) {
            hatchet.d("Emitting event: $event")
            internalUiEvents.tryEmit(event)
        }
    }

    protected fun <ListType, ReturnType> Flow<List<ListType>>.mapList(
        mapper: (ListType) -> ReturnType
    ): Flow<List<ReturnType>> {
        return map { list ->
            list.map(mapper)
        }
    }
}
