package com.vgleadsheets.features.main.tag

import com.vgleadsheets.features.main.ListRobot
import com.vgleadsheets.features.main.ListUiTest

class TagListRobot(test: ListUiTest): ListRobot(test) {
    init {
        checkScreenHeader("VGLeadSheets", "By Tag")
    }

    fun checkFirstTagIs(title: String, subtitle: String) {
        checkFirstItemHasTitleInternal(title)
        checkFirstItemHasSubtitleInternal(subtitle)
    }
}

fun tagList(
    test: ListUiTest,
    func: TagListRobot.() -> Unit
) = TagListRobot(test).apply {
    func()
}
