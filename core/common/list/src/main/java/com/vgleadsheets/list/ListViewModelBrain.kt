package com.vgleadsheets.list

import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.state.VglsAction
import com.vgleadsheets.ui.StringProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

abstract class ListViewModelBrain(
    private val stringProvider: StringProvider,
    private val hatchet: Hatchet,
    private val dispatchers: VglsDispatchers,
    private val coroutineScope: CoroutineScope,
) {
    abstract fun initialState(): ListState

    abstract fun handleAction(action: VglsAction)

    protected val internalUiEvents = MutableSharedFlow<ListEvent>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val uiEvents = internalUiEvents.asSharedFlow()

    protected val internalUiState = MutableStateFlow(initialState())
    private val internalUiStateActual = MutableStateFlow(initialState().toActual(stringProvider))
    val uiStateActual = internalUiStateActual
        .asStateFlow()

    protected fun updateState(updater: (ListState) -> ListState) {
        coroutineScope.launch(dispatchers.main) {
            val oldState = internalUiState.value
            val newState = updater(oldState)

            hatchet.v("Updating state: $newState")

            internalUiState.value = newState
            internalUiStateActual.value = newState.toActual(stringProvider)
        }
    }

    protected fun emitEvent(event: ListEvent) {
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
