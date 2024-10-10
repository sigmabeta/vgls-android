package com.vgleadsheets.dispatchers

import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.list.DelayManager
import com.vgleadsheets.settings.DebugSettingsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DelayManagerImpl(
    private val settingsManager: DebugSettingsManager,
    private val dispatchers: VglsDispatchers,
    private val coroutineScope: CoroutineScope,
) : DelayManager {
    private val shouldUseDelayState = MutableStateFlow(false)

    init {
        checkDelaySetting()
    }

    override fun shouldDelay() = shouldUseDelayState.value

    private fun checkDelaySetting() {
        settingsManager
            .getShouldDelay()
            .onEach { shouldUseDelayState.value = it ?: false }
            .flowOn(dispatchers.disk)
            .launchIn(coroutineScope)
    }
}
