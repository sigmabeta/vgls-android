package com.vgleadsheets.settings

import com.vgleadsheets.storage.common.Storage
import kotlinx.coroutines.flow.map

class DebugSettingsManager(
    private val storage: Storage
) {
    fun getShouldDelay() = getBoolean(SETTING_DELAY_LOADING_OPS)

    fun setShouldDelay(value: Boolean) = setBoolean(SETTING_DELAY_LOADING_OPS, value)

    private fun setBoolean(key: String, value: Boolean) {
        storage.saveString(key, value.toString())
    }

    private fun getBoolean(key: String) = storage
        .savedStringFlow(key)
        .map { it?.toBooleanStrictOrNull() }

    companion object {
        private const val SETTING_DELAY_LOADING_OPS = "setting.debug.delay"
    }
}
