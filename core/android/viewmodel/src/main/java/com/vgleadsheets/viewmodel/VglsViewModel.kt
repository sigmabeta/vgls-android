package com.vgleadsheets.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class VglsViewModel<StateType, EventType>(initialState: StateType): ViewModel() {
    protected val _uiState = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    protected val _uiEvents = MutableSharedFlow<EventType>()
    val uiEvents = _uiEvents.asSharedFlow()
}
