package com.vgleadsheets.features.main.game

import com.vgleadsheets.features.main.ListRobot
import com.vgleadsheets.features.main.ListUiTest

class GameRobot(
    test: ListUiTest,
    gameTitle: String,
    sheetCountText: String
): ListRobot(test) {
    init {
        checkScreenHeader(gameTitle, sheetCountText)
    }

    fun checkFirstSongIs(title: String): GameRobot {
        checkFirstItemHasTitleInternal(title)
        return this
    }

    fun clickSongWithTitle(title: String) {
        clickItemWithTitle(title)
    }
}

fun game(
    test: ListUiTest,
    gameTitle: String,
    sheetCountText: String,
    func: GameRobot.() -> Unit
) = GameRobot(test, gameTitle, sheetCountText).apply {
    func()
}
