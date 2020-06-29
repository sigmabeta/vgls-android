package com.vgleadsheets.features.main.settings

import com.vgleadsheets.features.main.ListRobot
import com.vgleadsheets.features.main.ListUiTest

class SettingsRobot(test: ListUiTest) : ListRobot(test) {
    init {
        isHeaderWithTitleDisplayed("Sheets", 0)
    }

    fun checkBooleanSettingValueIs(
        title: String,
        value: Boolean,
        scrollPosition: Int? = null
    ) {
        checkBooleanSettingValueIsInternal(title, value, scrollPosition)
    }
}

fun settings(
    test: ListUiTest,
    func: SettingsRobot.() -> Unit
) = SettingsRobot(test).apply {
    func()
}
