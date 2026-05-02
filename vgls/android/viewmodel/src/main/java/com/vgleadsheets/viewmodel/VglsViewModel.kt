package com.vgleadsheets.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.analytics.AnalyticsScreen
import net.sigmabeta.sage.analytics.isInitAction
import net.sigmabeta.sage.appcomm.ActionSink
import net.sigmabeta.sage.appcomm.EventDispatcher
import net.sigmabeta.sage.appcomm.EventSink
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.appcomm.SageEvent
import net.sigmabeta.sage.appcomm.SageState
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.debug.ShowDebugProvider
import net.sigmabeta.sage.list.DelayManager
import net.sigmabeta.sage.list.VglsScheduler
import net.sigmabeta.sage.logging.Hatchet

abstract class VglsViewModel<StateType : SageState> :
    ViewModel(),
    ActionSink,
    EventSink {
    protected abstract val hatchet: Hatchet
    protected abstract val analytics: Analytics
    protected abstract val dispatchers: SageDispatchers
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

    protected val internalUiEvents = MutableSharedFlow<SageEvent>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val uiEventsJob = internalUiEvents
        .onEach { eventDispatcher.sendEvent(it) }
        .launchIn(viewModelScope)

    abstract val screenIdentifier: AnalyticsScreen?

    abstract fun initialState(): StateType

    protected abstract fun handleAction(action: SageAction)

    protected abstract fun handleEvent(event: SageEvent)

    protected abstract fun sendInitAction()

    override fun sendAction(action: SageAction) {
        if (screenIdentifier != null) {
            if (action.isInitAction()) {
                analytics.logScreenView(action, screenIdentifier!!)
            } else {
                analytics.logVglsAction(action, screenIdentifier!!)
            }
        }

        if (action is SageAction.DeviceBack) {
            emitEvent(SageEvent.NavigateBack(this.javaClass.simpleName))
            return
        }

        viewModelScope.launch(scheduler.dispatchers.main) {
            handleAction(action)
        }
    }

    override fun sendEvent(event: SageEvent) {
        viewModelScope.launch(scheduler.dispatchers.main) {
            handleEvent(event)
        }
    }

    override fun onCleared() {
        eventDispatcher.removeEventSink(this)
    }

    protected fun emitEvent(event: SageEvent) {
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
    ): Flow<List<ReturnType>> = map { list ->
            list.map(mapper)
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
