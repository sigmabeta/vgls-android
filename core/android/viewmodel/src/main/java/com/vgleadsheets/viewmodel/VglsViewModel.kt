package com.vgleadsheets.viewmodel

import androidx.lifecycle.ViewModel
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.EventSink
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.appcomm.VglsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

abstract class VglsViewModel<StateType : VglsState> :
    ViewModel(),
    ActionSink,
    EventSink {
    protected val internalUiState = MutableStateFlow(initialState())
    val uiState = internalUiState.asStateFlow()

    val uiEvents = MutableSharedFlow<VglsEvent>()

    abstract fun initialState(): StateType

    protected fun <ListType, ReturnType> Flow<List<ListType>>.mapList(
        mapper: (ListType) -> ReturnType
    ): Flow<List<ReturnType>> {
        return map { list ->
            list.map(mapper)
        }
    }
}
