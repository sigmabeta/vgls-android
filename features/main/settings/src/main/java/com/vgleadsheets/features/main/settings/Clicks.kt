package com.vgleadsheets.features.main.settings

import com.vgleadsheets.features.main.list.ListItemClicks
import com.vgleadsheets.nav.Navigator

class Clicks(
    private val viewModel: SettingViewModel,
    private val navigator: Navigator
) : ListItemClicks {
    fun boolean(settingId: String, newValue: Boolean) {
        viewModel.setBooleanSetting(settingId, newValue)
    }

    fun dropdownSelection(settingId: String, selectedPos: Int) {
        viewModel.setDropdownSetting(settingId, selectedPos)
    }

    fun about() {
        navigator.showAbout()
    }
}
