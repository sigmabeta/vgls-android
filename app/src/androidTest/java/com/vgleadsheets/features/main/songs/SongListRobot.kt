package com.vgleadsheets.features.main.songs

import com.vgleadsheets.features.main.ListRobot
import com.vgleadsheets.features.main.ListUiTest

class SongListRobot(test: ListUiTest) : ListRobot(test) {
    init {
        checkScreenHeader("VGLeadSheets", "All Sheets")
    }

    override val maxScrolls = 6

    fun checkIsEmptyStateDisplayed(emptyStateLabel: String): SongListRobot {
        checkIsEmptyStateDisplayedInternal(emptyStateLabel)
        return this
    }

    fun checkFirstSongIs(title: String): SongListRobot {
        checkFirstItemHasTitleInternal(title)
        return this
    }

    fun clickSongWithTitle(title: String) {
        clickItemWithText(title)
    }
}

fun songList(
    test: ListUiTest,
    func: SongListRobot.() -> Unit
) = SongListRobot(test).apply {
    func()
}
