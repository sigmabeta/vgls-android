package com.vgleadsheets.features.main.tag.value

import com.vgleadsheets.features.main.ListRobot
import com.vgleadsheets.features.main.ListUiTest

class TagValueListRobot(
    test: ListUiTest,
    tagKey: String,
    sheetCountText: String
) : ListRobot(test) {
    init {
        checkScreenHeader(tagKey, sheetCountText)
    }

    fun checkFirstTagValueIs(title: String, subtitle: String) {
        checkFirstItemHasTitleInternal(title)
        checkFirstItemHasSubtitleInternal(subtitle)
    }
}

fun tagValueList(
    test: ListUiTest,
    tagKey: String,
    sheetCountText: String,
    func: TagValueListRobot.() -> Unit
) = TagValueListRobot(test, tagKey, sheetCountText).apply {
    func()
}
