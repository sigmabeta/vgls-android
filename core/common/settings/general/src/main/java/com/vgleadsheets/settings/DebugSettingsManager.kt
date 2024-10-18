package com.vgleadsheets.settings

import com.vgleadsheets.storage.common.Storage
import kotlinx.coroutines.flow.map

class DebugSettingsManager(
    private val storage: Storage
) {
    fun getShouldShowDebug() = getBoolean(SETTING_SHOW_DEBUG_OPTIONS, false)

    fun setShouldShowDebug(value: Boolean) = setBoolean(SETTING_SHOW_DEBUG_OPTIONS, value)

    fun getShouldUseFakeApi() = getBoolean(SETTING_USE_FAKE_API, false)

    fun setShouldUseFakeApi(value: Boolean) = setBoolean(SETTING_USE_FAKE_API, value)

    fun getShouldDelay() = getBoolean(SETTING_DELAY_LOADING_OPS, false)

    fun setShouldDelay(value: Boolean) = setBoolean(SETTING_DELAY_LOADING_OPS, value)

    fun getShouldShowSnackbars() = getBoolean(SETTING_NAV_SHOW_SNACKBARS, false)

    fun setShouldShowSnackbars(value: Boolean) = setBoolean(SETTING_NAV_SHOW_SNACKBARS, value)

    private fun setBoolean(key: String, value: Boolean) {
        storage.saveString(key, value.toString())
    }

    private fun getBoolean(key: String, default: Boolean) = storage
        .savedStringFlow(key)
        .map { it?.toBooleanStrictOrNull() ?: default }

    companion object {
        private const val SETTING_SHOW_DEBUG_OPTIONS = "setting.debug.visible"
        private const val SETTING_DELAY_LOADING_OPS = "setting.debug.delay"
        private const val SETTING_USE_FAKE_API = "setting.debug.fake"
        private const val SETTING_NAV_SHOW_SNACKBARS = "setting.debug.nav.snackbars"
    }
}
