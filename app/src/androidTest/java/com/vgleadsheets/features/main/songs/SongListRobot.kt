package com.vgleadsheets.features.main.songs

import com.vgleadsheets.features.main.ListRobot
import com.vgleadsheets.features.main.ListUiTest

class SongListRobot(test: ListUiTest) : ListRobot(test) {
    init {
        checkScreenHeader("VGLeadSheets", "All Sheets")
    }

    fun checkIsEmptyStateDisplayed(emptyStateLabel: String) {
        checkIsEmptyStateDisplayedInternal(emptyStateLabel)
    }

    fun checkFirstSongIs(title: String) {
        checkFirstItemHasTitleInternal(title)
    }
}

fun songList(
    test: ListUiTest,
    func: SongListRobot.() -> Unit
) = SongListRobot(test).apply {
    func()
}
