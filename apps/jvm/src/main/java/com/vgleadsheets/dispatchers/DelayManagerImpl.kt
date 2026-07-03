package com.vgleadsheets.dispatchers

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.list.DelayManager
import net.sigmabeta.sage.settings.DebugSettingsManager

/**
 * Desktop copy of the android app's DelayManagerImpl (pure coroutines/settings logic — it just lives
 * in each app module rather than a shared one). Backs the debug "artificial delay" toggle.
 */
class DelayManagerImpl(
    private val settingsManager: DebugSettingsManager,
    private val dispatchers: SageDispatchers,
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
