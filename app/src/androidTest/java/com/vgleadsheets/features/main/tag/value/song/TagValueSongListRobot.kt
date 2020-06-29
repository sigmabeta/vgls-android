package com.vgleadsheets.features.main.tag.value.song

import com.vgleadsheets.features.main.ListRobot
import com.vgleadsheets.features.main.ListUiTest

class TagValueSongListRobot(
    test: ListUiTest,
    tagValue: String,
    sheetCountText: String
) : ListRobot(test) {
    init {
        checkScreenHeader(tagValue, sheetCountText)
    }

    fun checkFirstSongIs(title: String) {
        checkFirstItemHasTitleInternal(title)
    }

    fun checkFirstSongGameIs(name: String) {
        checkFirstItemHasSubtitleInternal(name)
    }
}

fun tagValueSongList(
    test: ListUiTest,
    tagValue: String,
    sheetCountText: String,
    func: TagValueSongListRobot.() -> Unit
) = TagValueSongListRobot(test, tagValue, sheetCountText).apply {
    func()
}
