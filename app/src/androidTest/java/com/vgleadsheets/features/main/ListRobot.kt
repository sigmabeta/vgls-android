package com.vgleadsheets.features.main

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withChild
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.vgleadsheets.R
import com.vgleadsheets.RecyclerViewMatcher
import com.vgleadsheets.Robot
import com.vgleadsheets.components.ComponentViewHolder
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher

abstract class ListRobot(test: ListUiTest) : Robot(test) {
    protected val resources by lazy { test.activityRule.activity.resources }

    fun isHeaderWithTitleDisplayed(text: String, scrollPosition: Int? = null) {
        scrollHelper(scrollPosition) {
            isHeaderWithTitleDisplayedHelper(text)
        }
    }

    fun isItemWithTitleDisplayed(text: String, scrollPosition: Int? = null) {
        scrollHelper(scrollPosition) {
            isItemWithTitleDisplayedHelper(text)
        }
    }

    protected fun clickItemWithTitle(text: String, scrollPosition: Int? = null) {
        scrollHelper(scrollPosition) {
            clickItemWithTitleHelper(text)
        }
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

    protected fun checkBooleanSettingValueIsInternal(
        title: String,
        value: Boolean,
        scrollPosition: Int? = null
    ) {
        scrollHelper(scrollPosition) {
            val checkBox = onView(
                allOf(
                    withParent(
                        withChild(
                            allOf(
                                withId(
                                    R.id.text_name
                                ),
                                withText(
                                    title
                                )
                            )
                        )
                    ),
                    withId(
                        R.id.checkbox_setting
                    )
                )
            )

            if (value) {
                checkBox.check(
                    matches(
                        ViewMatchers.isChecked()
                    )
                )
            } else {
                checkBox.check(
                    matches(
                        ViewMatchers.isNotChecked()
                    )
                )
            }
        }
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

    private fun isHeaderWithTitleDisplayedHelper(text: String) {
        onView(
            allOf(
                withId(
                    R.id.text_header_name
                ),
                withText(
                    text
                )
            )
        ).check(
            matches(
                isDisplayed()
            )
        )
    }

    private fun isItemWithTitleDisplayedHelper(text: String) {
        onView(
            allOf(
                withId(
                    R.id.text_name
                ),
                withText(
                    text
                )
            )
        ).check(
            matches(
                isDisplayed()
            )
        )
    }

    private fun clickItemWithTitleHelper(text: String) {
        onView(
            allOf(
                withId(
                    R.id.text_name
                ),
                withText(
                    text
                )
            )
        ).perform(
            click()
        )
    }

    private fun scrollHelper(scrollPosition: Int?, afterScroll: () -> Unit) {
        if (scrollPosition != null) {
            scrollTo(0)
            scrollTo(scrollPosition)
        }

        afterScroll()
    }

    private fun scrollTo(scrollPosition: Int) {
        onView(
            withId(
                R.id.list_content
            )
        ).perform(
            RecyclerViewActions.scrollToPosition<ComponentViewHolder>(scrollPosition)
        )
    }
}
