package com.vgleadsheets.features.main.debug

import com.vgleadsheets.features.main.list.ListItemClicks

class Clicks(
    private val viewModel: DebugViewModel,
) : ListItemClicks {
    fun boolean(settingId: String, newValue: Boolean) {
        viewModel.onBooleanSettingClicked(settingId, newValue)
    }

    fun dropdownSelection(settingId: String, selectedPos: Int) {
        viewModel.onDropdownSettingSelected(settingId, selectedPos)
    }

    fun clearSheets() {
        viewModel.clearSheets()
    }
}
