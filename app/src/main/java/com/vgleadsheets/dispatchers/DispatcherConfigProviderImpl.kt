package com.vgleadsheets.dispatchers

import com.vgleadsheets.coroutines.DispatcherConfigProvider
import com.vgleadsheets.settings.DebugSettingsManager
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

class DispatcherConfigProviderImpl(
    private val settingsManager: DebugSettingsManager,
) : DispatcherConfigProvider {
    override fun shouldUseDelayDispatcher(): Boolean {
        return runBlocking {
            val setting = settingsManager
                .getShouldDelay()
                .firstOrNull()

            return@runBlocking setting ?: false
        }
    }
}
