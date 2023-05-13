package com.vgleadsheets.features.main.hud

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.vgleadsheets.R
import com.vgleadsheets.Robot
import com.vgleadsheets.UiTest

class HudRobot(test: UiTest) : Robot(test) {
    fun clickSearch() {
        clickView(R.id.text_search_hint)
    }

    fun checkSearchButtonIsBackArrow() {
        onView(
            ViewMatchers.withId(R.id.button_search_menu_back)
        ).check(
            ViewAssertions.matches(
                ViewMatchers.withContentDescription(com.vgleadsheets.features.main.hud.R.string.cd_search_back)
            )
        )
    }

    fun checkSearchButtonIsCross() {
        onView(
            ViewMatchers.withId(R.id.button_search_menu_back)
        ).check(
            ViewAssertions.matches(
                ViewMatchers.withContentDescription(com.vgleadsheets.features.main.hud.R.string.cd_search_close)
            )
        )
    }

    fun checkSearchButtonIsHamburger() {
        onView(
            ViewMatchers.withId(R.id.button_search_menu_back)
        ).check(
            ViewAssertions.matches(
                ViewMatchers.withContentDescription(com.vgleadsheets.features.main.hud.R.string.cd_search_menu)
            )
        )
    }
}

fun hud(
    test: UiTest,
    func: HudRobot.() -> Unit
) = HudRobot(test).apply {
    func()
}
