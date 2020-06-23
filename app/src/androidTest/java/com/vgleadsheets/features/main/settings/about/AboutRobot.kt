package com.vgleadsheets.features.main.settings.about

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.vgleadsheets.R
import com.vgleadsheets.features.main.ListRobot
import com.vgleadsheets.features.main.ListUiTest
import org.hamcrest.Matchers.allOf

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

    fun checkWebBrowserLaunchedUrl(url: String) {
        intended(
            allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData(url)
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
