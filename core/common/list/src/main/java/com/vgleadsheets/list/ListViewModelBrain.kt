package com.vgleadsheets.list

import com.vgleadsheets.state.VglsAction
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

abstract class ListViewModelBrain {
    abstract fun initialState(): ListState

    abstract fun handleAction(action: VglsAction)

    protected val internalUiEvents = MutableSharedFlow<ListEvent>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val uiEvents = internalUiEvents.asSharedFlow()

    protected val internalUiState = MutableStateFlow(initialState())
    val uiState = internalUiState.asStateFlow()

    protected fun emitEvent(event: ListEvent) {
        internalUiEvents.tryEmit(event)
    }

    protected fun <ListType, ReturnType> Flow<List<ListType>>.mapList(
        mapper: (ListType) -> ReturnType
    ): Flow<List<ReturnType>> {
        return map { list ->
            list.map(mapper)
        }
    }
}
