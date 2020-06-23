package com.vgleadsheets.features.main.songs

import com.vgleadsheets.features.main.ListRobot
import com.vgleadsheets.features.main.ListUiTest

class SongListRobot(test: ListUiTest) : ListRobot(test) {
    init {
        checkScreenHeader("VGLeadSheets", "All Sheets")
    }

    fun checkIsEmptyStateDisplayed(emptyStateLabel: String): SongListRobot {
        checkIsEmptyStateDisplayedInternal(emptyStateLabel)
        return this
    }

    fun checkFirstSongIs(title: String): SongListRobot {
        checkFirstItemHasTitleInternal(title)
        return this
    }
}

fun songList(
    test: ListUiTest,
    func: SongListRobot.() -> Unit
) = SongListRobot(test).apply {
    func()
}
