package com.vgleadsheets.features.main.debug

import com.vgleadsheets.features.main.list.ListItemClicks

internal class Clicks(
    val viewModel: DebugViewModel,
) : ListItemClicks {
    fun onBooleanSettingClicked(settingId: String, newValue: Boolean) {
        viewModel.onBooleanSettingClicked(settingId, newValue)
    }

    fun onDropdownSettingSelected(settingId: String, selectedPos: Int) {
        viewModel.onDropdownSettingSelected(settingId, selectedPos)
    }

    fun clearSheets() {
        viewModel.clearSheets()
    }

    fun clearJams() {
        viewModel.clearJams()
    }
}
