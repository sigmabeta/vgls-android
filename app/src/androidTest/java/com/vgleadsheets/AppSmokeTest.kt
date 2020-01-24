package com.vgleadsheets

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.vgleadsheets.main.MainActivity
import org.junit.Rule
import org.junit.Test

@LargeTest
class AppSmokeTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun appLaunchesAndTitleIsVisible() {
        onView(withId(R.id.text_title_title))
            .check(matches(withText(R.string.app_name)))

        onView(withId(R.id.text_title_subtitle))
            .check(matches(withText(R.string.subtitle_game)))
    }
}
