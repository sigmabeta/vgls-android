package com.vgleadsheets.features.main.settings

import androidx.annotation.StringRes
import com.vgleadsheets.features.main.ListRobot
import com.vgleadsheets.features.main.ListUiTest

class SettingsRobot(test: ListUiTest) : ListRobot(test) {
    fun clickSettingWithTitle(@StringRes titleId: Int, scrollPosition: Int? = null) {
        val titleString = resources.getString(titleId)
        clickItemWithTitle(titleString, scrollPosition)
    }

    fun checkBooleanSettingValueIs(
        @StringRes titleId: Int,
        value: Boolean,
        scrollPosition: Int? = null
    ) {
        val titleString = resources.getString(titleId)
        checkBooleanSettingValueIsInternal(titleString, value, scrollPosition)
    }
}

fun settings(
    test: ListUiTest,
    func: SettingsRobot.() -> Unit
) = SettingsRobot(test).apply {
    func()
}
