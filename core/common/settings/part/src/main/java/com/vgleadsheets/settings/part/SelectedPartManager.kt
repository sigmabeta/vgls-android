package com.vgleadsheets.settings.part

import com.vgleadsheets.model.Part
import com.vgleadsheets.settings.common.Storage
import kotlinx.coroutines.flow.map

class SelectedPartManager(
    private val storage: Storage
) {
    fun setPart(part: Part) {
        storage.saveInt(SETTING_PART, part.ordinal)
    }

    fun selectedPartFlow() = storage.savedIntFlow(SETTING_PART)
        .map {
            if (it == null) {
                Part.C
            } else if (it < 0 || it >= Part.entries.size) {
                Part.C
            } else {
                Part.entries[it]
            }
        }

    companion object {
        private const val SETTING_PART = "setting.part"
    }
}
