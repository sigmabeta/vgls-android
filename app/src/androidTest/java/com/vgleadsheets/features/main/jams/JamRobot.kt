package com.vgleadsheets.features.main.jams

import com.vgleadsheets.features.main.ListRobot
import com.vgleadsheets.features.main.ListUiTest

class JamRobot(
    test: ListUiTest,
    jamName: String
) : ListRobot(test) {
    init {
        checkScreenHeader(jamName, "Jam Session")
    }
}

fun jam(
    test: ListUiTest,
    jamName: String,
    func: JamRobot.() -> Unit
) = JamRobot(test, jamName).apply {
    func()
}
