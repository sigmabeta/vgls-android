package com.vgleadsheets.features.main

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.vgleadsheets.R
import com.vgleadsheets.RecyclerViewMatcher
import org.hamcrest.Matcher

abstract class ListRobot(val test: ListUiTest) {
    init {
        test.launchScreen()
    }

    protected fun checkIsEmptyStateDisplayedInternal(emptyStateLabel: String) {
        checkFirstContentItem(
            hasDescendant(
                    withText("No $emptyStateLabel found at all. Check your internet connection?")
            )
        )
    }

    protected fun checkFirstItemHasTitleInternal(title: String) {
        checkFirstContentItem(
            hasDescendant(
                withText(title)
            )
        )
    }

    protected fun checkFirstItemHasSubtitleInternal(subtitle: String) {
        checkFirstContentItem(
            hasDescendant(
                withText(subtitle)
            )
        )
    }

    protected fun clickItemWithTitleInternal(title: String) {
        onView(
            withText(
                title
            )
        ).perform(
            click()
        )
    }

    protected fun checkScreenHeader(title: String, subtitle: String) {
        checkViewText(R.id.text_title_title, title)
        checkViewText(R.id.text_title_subtitle, subtitle)
    }

    protected fun checkViewText(viewId: Int, text: String) {
        onView(
            withId(viewId)
        ).check(
            matches(
                withText(text)
            )
        )
    }

    private fun checkFirstContentItem(
        matcher: Matcher<View>?
    ) {
        val contentView = RecyclerViewMatcher()

        onView(
            contentView.atPosition(1)
        ).check(
            matches(
                matcher
            )
        )
    }
}
