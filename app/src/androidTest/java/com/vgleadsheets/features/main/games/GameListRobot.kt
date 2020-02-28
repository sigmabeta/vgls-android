package com.vgleadsheets.features.main.games

import com.vgleadsheets.features.main.ListRobot
import com.vgleadsheets.features.main.ListUiTest

class GameListRobot(test: ListUiTest): ListRobot(test) {
    init {
        checkScreenHeader("VGLeadSheets", "By Game")
    }

    fun checkIsEmptyStateDisplayed(emptyStateLabel: String): GameListRobot {
        checkIsEmptyStateDisplayedInternal(emptyStateLabel)
        return this
    }

    fun checkFirstGameIs(title: String): GameListRobot {
        checkFirstItemHasTitleInternal(title)
        return this
    }

    fun clickItemWithTitle(title: String) {
        clickItemWithTitleInternal(title)
    }
}

fun gameList(
    test: ListUiTest,
    func: GameListRobot.() -> Unit
) = GameListRobot(test).apply {
    func()
}

