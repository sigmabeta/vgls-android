package com.vgleadsheets.features.main.sheet

import com.vgleadsheets.features.main.ListRobot
import com.vgleadsheets.features.main.ListUiTest

class SheetDetailRobot(
    test: ListUiTest,
    sheetTitle: String,
    pageCountText: String
) : ListRobot(test) {
    init {
        checkScreenHeader(sheetTitle, pageCountText)
    }

    fun clickViewSheet() {
        clickCtaWithTitle("View Sheet", 1)
    }

    fun clickYoutube() {
        clickCtaWithTitle("Search Youtube for this song", 2)
    }

    fun clickGame() {
        clickLabelValueWithLabel("Game", 4)
    }

    fun clickComposer() {
        clickLabelValueWithLabel("Composer", 5)
    }
}

fun sheetDetail(
    test: ListUiTest,
    sheetTitle: String,
    pageCountText: String,
    func: SheetDetailRobot.() -> Unit
) = SheetDetailRobot(test, sheetTitle, pageCountText).apply {
    func()
}
