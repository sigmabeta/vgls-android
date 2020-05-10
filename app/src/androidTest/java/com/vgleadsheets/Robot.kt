package com.vgleadsheets

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
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

    fun checkViewText(viewId: Int, textId: Int) {
        onView(
            withId(viewId)
        ).check(
            matches(
                withText(textId)
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

    fun checkScreenHeader(title: String, subtitle: String) {
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
}

