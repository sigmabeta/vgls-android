package com.vgleadsheets.list

import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.ui.StringProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

abstract class ListViewModelBrain(
    private val stringProvider: StringProvider,
    private val hatchet: Hatchet,
    private val scheduler: VglsScheduler,
) {
    abstract fun initialState(): ListState

    protected abstract fun handleAction(action: VglsAction)

    protected open fun handleEvent(event: VglsEvent) {}

    protected val internalUiEvents = MutableSharedFlow<VglsEvent>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val uiEvents = internalUiEvents.asSharedFlow()

    protected val internalUiState = MutableStateFlow(initialState())
    private val internalUiStateActual = MutableStateFlow(initialState().toActual(stringProvider))
    val uiStateActual = internalUiStateActual
        .asStateFlow()

    fun sendAction(action: VglsAction) {
        hatchet.d("${this.javaClass.simpleName} - Handling action: $action")

        if (action is VglsAction.DeviceBack) {
            emitEvent(VglsEvent.NavigateBack(this.javaClass.simpleName))
            return
        }

        if (action is VglsAction.Resume) {
            emitEvent(VglsEvent.ShowUiChrome)

            val state = internalUiState.value
            val titleModel = state.title(stringProvider)

            if (titleModel.title != null) {
                emitEvent(
                    VglsEvent.UpdateTitle(
                        title = titleModel.title,
                        subtitle = titleModel.subtitle,
                        shouldShowBack = titleModel.shouldShowBack,
                        source = this.javaClass.simpleName
                    )
                )
            }
        }

        scheduler.coroutineScope.launch(scheduler.dispatchers.main) {
            handleAction(action)
        }
    }

    fun sendEvent(event: VglsEvent) {
        scheduler.coroutineScope.launch(scheduler.dispatchers.main) {
            hatchet.d("${this@ListViewModelBrain.javaClass.simpleName} - Handling event: $event")
            handleEvent(event)
        }
    }

    protected fun updateState(updater: (ListState) -> ListState) {
        scheduler.coroutineScope.launch(scheduler.dispatchers.main) {
            val oldState = internalUiState.value
            val newState = updater(oldState)

            internalUiState.value = newState
            internalUiStateActual.value = newState.toActual(stringProvider)
        }
    }

    protected fun emitEvent(event: VglsEvent) {
        scheduler.coroutineScope.launch(scheduler.dispatchers.main) {
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

    @Suppress("MagicNumber")
    protected fun <EmissionType> Flow<EmissionType>.runInBackground(
        dispatcher: CoroutineDispatcher = scheduler.dispatchers.disk,
        shouldDelay: Boolean = scheduler.delayManager.shouldDelay()
    ): Job {
        val possiblyDelayedFlow = if (shouldDelay) {
            (
                this.onStart { delay(2000L) }
                )
        } else {
            this
        }

        return possiblyDelayedFlow
            .flowOn(dispatcher)
            .launchIn(scheduler.coroutineScope)
    }
}
