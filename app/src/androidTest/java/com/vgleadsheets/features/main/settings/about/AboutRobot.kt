package com.vgleadsheets.features.main.settings.about

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.vgleadsheets.R
import com.vgleadsheets.features.main.ListRobot
import com.vgleadsheets.features.main.ListUiTest

class AboutRobot(test: ListUiTest) : ListRobot(test) {
    init {
        isHeaderWithTitleDisplayed("About VGLeadSheets", 0)
    }

    fun checkLicenseViewIsVisible() {
        onView(
            withId(R.id.web_license)
        ).check(
            matches(
                isDisplayed()
            )
        )
    }
}

fun about(
    test: ListUiTest,
    func: AboutRobot.() -> Unit
) = AboutRobot(test).apply {
    func()
}
