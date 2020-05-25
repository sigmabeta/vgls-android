package com.vgleadsheets.features.main.games

import com.vgleadsheets.features.main.ListRobot
import com.vgleadsheets.features.main.ListUiTest

class GameListRobot(test: ListUiTest) : ListRobot(test) {
    init {
        checkScreenHeader("VGLeadSheets", "By Game")
    }

    override val maxScrolls = 2

    fun checkIsEmptyStateDisplayed(emptyStateLabel: String): GameListRobot {
        checkIsEmptyStateDisplayedInternal(emptyStateLabel)
        return this
    }

    fun checkFirstGameIs(title: String): GameListRobot {
        checkFirstItemHasTitleInternal(title)
        return this
    }

    fun clickGameWithTitle(title: String, scrollPosition: Int? = null) {
        clickItemWithTitle(title, scrollPosition)
    }
}

fun gameList(
    test: ListUiTest,
    func: GameListRobot.() -> Unit
) = GameListRobot(test).apply {
    func()
}
