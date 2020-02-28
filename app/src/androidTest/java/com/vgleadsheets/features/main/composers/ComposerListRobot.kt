package com.vgleadsheets.features.main.composers

import com.vgleadsheets.features.main.ListRobot
import com.vgleadsheets.features.main.ListUiTest

class ComposerListRobot(test: ListUiTest): ListRobot(test) {
    init {
        checkScreenHeader("VGLeadSheets", "By Composer")
    }

    fun checkIsEmptyStateDisplayed(emptyStateLabel: String): ComposerListRobot {
        checkIsEmptyStateDisplayedInternal(emptyStateLabel)
        return this
    }

    fun checkFirstComposerIs(title: String): ComposerListRobot {
        checkFirstItemHasTitleInternal(title)
        return this
    }

    fun clickItemWithTitle(title: String) {
        clickItemWithTitleInternal(title)
    }
}

fun composerList(
    test: ListUiTest,
    func: ComposerListRobot.() -> Unit
) = ComposerListRobot(test).apply {
    func()
}
