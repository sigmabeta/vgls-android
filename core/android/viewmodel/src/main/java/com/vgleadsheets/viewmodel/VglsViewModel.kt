package com.vgleadsheets.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.EventDispatcher
import com.vgleadsheets.appcomm.EventSink
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.appcomm.VglsState
import com.vgleadsheets.common.debug.ShowDebugProvider
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.list.DelayManager
import com.vgleadsheets.list.VglsScheduler
import com.vgleadsheets.logging.Hatchet
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

abstract class VglsViewModel<StateType : VglsState> :
    ViewModel(),
    ActionSink,
    EventSink {
    protected abstract val hatchet: Hatchet
    protected abstract val dispatchers: VglsDispatchers
    protected abstract val delayManager: DelayManager
    protected abstract val eventDispatcher: EventDispatcher
    protected abstract val showDebugProvider: ShowDebugProvider
    val scheduler by lazy {
        object : VglsScheduler {
            override val dispatchers = this@VglsViewModel.dispatchers
            override val coroutineScope = viewModelScope
            override val delayManager = this@VglsViewModel.delayManager
        }
    }
    val showDebug by lazy { showDebugProvider.showDebugFlow }

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

        viewModelScope.launch(scheduler.dispatchers.main) {
            handleAction(action)
        }
    }

    override fun sendEvent(event: VglsEvent) {
        viewModelScope.launch(scheduler.dispatchers.main) {
            handleEvent(event)
        }
    }

    override fun onCleared() {
        eventDispatcher.removeEventSink(this)
    }

    protected fun emitEvent(event: VglsEvent) {
        viewModelScope.launch(scheduler.dispatchers.main) {
            hatchet.d("Emitting event: $event")
            internalUiEvents.tryEmit(event)
        }
    }

    protected open fun updateState(updater: (StateType) -> StateType) {
        viewModelScope.launch(scheduler.dispatchers.main) {
            val oldState = internalUiState.value
            val newState = updater(oldState)

            internalUiState.value = newState
        }
    }

    protected fun <ListType, ReturnType> Flow<List<ListType>>.mapList(
        mapper: (ListType) -> ReturnType
    ): Flow<List<ReturnType>> {
        return map { list ->
            list.map(mapper)
        }
    }

    @Suppress("MagicNumber")
    protected fun <EmissionType> Flow<EmissionType>.runInBackground(
        dispatcher: CoroutineDispatcher = scheduler.dispatchers.disk,
        shouldDelay: Boolean = scheduler.delayManager.shouldDelay()
    ): Job {
        val possiblyDelayedFlow = if (shouldDelay) {
            this.onStart { delay(5000L) }
        } else {
            this
        }

        return possiblyDelayedFlow
            .flowOn(dispatcher)
            .launchIn(scheduler.coroutineScope)
    }
}
