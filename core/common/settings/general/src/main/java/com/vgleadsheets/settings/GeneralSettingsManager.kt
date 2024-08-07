package com.vgleadsheets.settings

import com.vgleadsheets.storage.common.Storage
import kotlinx.coroutines.flow.map

class GeneralSettingsManager(
    private val storage: Storage
) {
    fun getKeepScreenOn() = getBoolean(SETTING_KEEP_SCREEN_ON)

    fun setKeepScreenOn(value: Boolean) = setBoolean(SETTING_KEEP_SCREEN_ON, value)

    private fun setBoolean(key: String, value: Boolean) {
        storage.saveString(key, value.toString())
    }

    private fun getBoolean(key: String) = storage
        .savedStringFlow(key)
        .map { it.toBoolean() }

    companion object {
        private const val SETTING_KEEP_SCREEN_ON = "setting.general.screen"
    }
}
