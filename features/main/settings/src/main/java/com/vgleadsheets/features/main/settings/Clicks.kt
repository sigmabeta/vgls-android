package com.vgleadsheets.features.main.settings

import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.features.main.list.ListItemClicks

class Clicks(
    private val viewModel: SettingViewModel,
    private val router: FragmentRouter
) : ListItemClicks {
    fun boolean(settingId: String, newValue: Boolean) {
        viewModel.setBooleanSetting(settingId, newValue)
    }

    fun dropdownSelection(settingId: String, selectedPos: Int) {
        viewModel.setDropdownSetting(settingId, selectedPos)
    }

    fun about() {
        router.showAbout()
    }
}
