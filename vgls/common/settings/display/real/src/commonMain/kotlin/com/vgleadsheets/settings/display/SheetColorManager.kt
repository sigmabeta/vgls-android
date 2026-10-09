package com.vgleadsheets.settings.display

import com.vgleadsheets.model.SheetColorMode
import kotlinx.coroutines.flow.map
import net.sigmabeta.sage.storage.common.Storage

class SheetColorManager(
    private val storage: Storage
) {
    fun setSheetColorMode(mode: SheetColorMode) {
        storage.saveString(SETTING_SHEET_COLOR, mode.name)
    }

    fun sheetColorModeFlow() = storage.savedStringFlow(SETTING_SHEET_COLOR)
        .map { stored ->
            SheetColorMode.entries.firstOrNull { it.name == stored } ?: SheetColorMode.LIGHT
        }

    companion object {
        private const val SETTING_SHEET_COLOR = "setting.display.sheet_color"
    }
}
