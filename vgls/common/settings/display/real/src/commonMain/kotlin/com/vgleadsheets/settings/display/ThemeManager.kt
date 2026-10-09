package com.vgleadsheets.settings.display

import com.vgleadsheets.model.ThemeMode
import kotlinx.coroutines.flow.map
import net.sigmabeta.sage.storage.common.Storage

class ThemeManager(
    private val storage: Storage
) {
    fun setThemeMode(mode: ThemeMode) {
        storage.saveString(SETTING_THEME_MODE, mode.name)
    }

    fun themeModeFlow() = storage.savedStringFlow(SETTING_THEME_MODE)
        .map { ThemeMode.fromStorageValue(it) }

    companion object {
        private const val SETTING_THEME_MODE = "setting.display.theme"
    }
}
