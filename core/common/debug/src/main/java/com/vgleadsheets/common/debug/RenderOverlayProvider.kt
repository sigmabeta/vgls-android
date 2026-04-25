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

class RenderOverlayProvider(
    private val debugSettingsManager: DebugSettingsManager,
    private val coroutineScope: CoroutineScope,
    private val dispatchers: VglsDispatchers,
) {
    private val _showRenderOverlayFlow = MutableStateFlow(false)
    val showRenderOverlayFlow = _showRenderOverlayFlow.asStateFlow()

    init {
        debugSettingsManager.getShouldShowRenderOverlay()
            .onEach { newValue -> _showRenderOverlayFlow.update { newValue } }
            .flowOn(dispatchers.disk)
            .launchIn(coroutineScope)
    }
}
