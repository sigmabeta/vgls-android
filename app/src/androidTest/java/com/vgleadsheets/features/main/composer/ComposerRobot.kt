package com.vgleadsheets.features.main.composer

import com.vgleadsheets.features.main.ListRobot
import com.vgleadsheets.features.main.ListUiTest

class ComposerRobot(
    test: ListUiTest,
    composerName: String,
    sheetCountText: String
) : ListRobot(test) {
    init {
        checkScreenHeader(composerName, sheetCountText)
    }

    override val maxScrolls = 2

    fun checkFirstSongIs(name: String): ComposerRobot {
        checkFirstItemHasTitleInternal(name)
        return this
    }

    fun checkFirstSongGameIs(name: String): ComposerRobot {
        checkFirstItemHasSubtitleInternal(name)
        return this
    }

    fun clickSongWithTitle(name: String, scrollPosition: Int? = null) {
        clickItemWithTitle(name, scrollPosition)
    }
}

fun composer(
    test: ListUiTest,
    composerName: String,
    sheetCountText: String,
    func: ComposerRobot.() -> Unit
) = ComposerRobot(test, composerName, sheetCountText).apply {
    func()
}
