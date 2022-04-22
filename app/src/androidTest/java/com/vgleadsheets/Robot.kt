package com.vgleadsheets

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers.allOf

abstract class Robot(val test: UiTest) {
    init {
        test.launchScreen()
    }

    fun clickView(id: Int) {
        onView(
            withId(id)
        ).perform(
            click()
        )
    }

    fun clickViewWithText(textId: Int) {
        onView(
            withText(textId)
        ).perform(
            click()
        )
    }

    fun checkViewText(viewId: Int, textId: Int) {
        onView(
            withId(viewId)
        ).check(
            matches(
                withText(textId)
            )
        )
    }

    fun checkViewText(textId: Int) {
        onView(
            withText(textId)
        ).check(
            matches(
                isDisplayed()
            )
        )
    }

    fun checkViewText(text: String) {
        onView(
            withText(text)
        ).check(
            matches(
                isDisplayed()
            )
        )
    }

    fun checkViewVisible(viewId: Int) {
        onView(
            withId(viewId)
        ).check(
            matches(
                isDisplayed()
            )
        )
    }

    fun checkViewWithIdAndTextVisible(viewId: Int, textId: Int) {
        onView(
            allOf(
                withId(viewId),
                withText(textId)
            )
        ).check(
            matches(
                isDisplayed()
            )
        )
    }

    fun checkViewNotVisible(viewId: Int) {
        onView(
            withId(viewId)
        ).check(
            matches(
                not(
                    isDisplayed()
                )
            )
        )
    }

    fun checkViewText(viewId: Int, text: String) {
        onView(
            withId(viewId)
        ).check(
            matches(
                withText(text)
            )
        )
    }

    open fun checkScreenHeader(title: String, subtitle: String) {
        checkViewText(R.id.text_title_title, title)
        checkViewText(R.id.text_title_subtitle, subtitle)
    }

    fun checkTopLevelScreen(titleStringId: Int, subtitleStringId: Int?) {
        checkViewVisible(R.id.text_title_title)
        checkViewVisible(R.id.text_title_subtitle)

        checkViewText(R.id.text_title_title, titleStringId)

        if (subtitleStringId != null) {
            checkViewText(R.id.text_title_subtitle, subtitleStringId)
        }
    }

    fun checkWebBrowserLaunchedUrl(url: String) {
        if (test.activityRule !is IntentsTestRule) {
            throw IllegalStateException("Cannot run this assertion unless it's an IntentsTestRule.")
        }

        intended(
            allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData(url)
            )
        )
    }
}
