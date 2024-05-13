package com.vgleadsheets.viewmodel

import androidx.lifecycle.ViewModel
import com.vgleadsheets.state.VglsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

abstract class VglsViewModel<StateType: VglsState, EventType>() : ViewModel() {
    protected val internalUiState = MutableStateFlow(initialState())
    val uiState = internalUiState.asStateFlow()

    val uiEvents = MutableSharedFlow<EventType>()

    abstract fun initialState(): StateType

    protected fun <ListType, ReturnType> Flow<List<ListType>>.mapList(
        mapper: (ListType) -> ReturnType
    ): Flow<List<ReturnType>> {
        return map { list ->
            list.map(mapper)
        }
    }
}
