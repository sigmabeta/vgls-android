package com.vgleadsheets.common.debug

import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.settings.DebugSettingsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class ShowDebugProvider(
    private val debugSettingsManager: DebugSettingsManager,
    private val coroutineScope: CoroutineScope,
    private val dispatchers: VglsDispatchers,
) {
    private val _showDebugFlow = MutableStateFlow(false)
    val showDebugFlow = _showDebugFlow.asStateFlow()

    init {
        debugSettingsManager.getShouldShowDebug()
            .onEach { newValue ->
                _showDebugFlow.update { newValue ?: false }
            }
            .flowOn(dispatchers.disk)
            .launchIn(coroutineScope)
    }
}
