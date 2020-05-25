package com.vgleadsheets.features.main.game

import com.vgleadsheets.features.main.ListRobot
import com.vgleadsheets.features.main.ListUiTest

class GameRobot(
    test: ListUiTest,
    gameTitle: String,
    sheetCountText: String
) : ListRobot(test) {
    init {
        checkScreenHeader(gameTitle, sheetCountText)
    }

    override val maxScrolls = 2

    fun checkFirstSongIs(title: String): GameRobot {
        checkFirstItemHasTitleInternal(title)
        return this
    }

    fun checkFirstSongArtistIs(name: String): GameRobot {
        checkFirstItemHasSubtitleInternal(name)
        return this
    }

    fun clickSongWithTitle(title: String, scrollPosition: Int? = null) {
        clickItemWithTitle(title, scrollPosition)
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
