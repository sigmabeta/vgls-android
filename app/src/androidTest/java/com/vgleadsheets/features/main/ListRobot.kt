package com.vgleadsheets.features.main

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.PerformException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.vgleadsheets.R
import com.vgleadsheets.RecyclerViewMatcher
import org.hamcrest.Matcher
import timber.log.Timber

abstract class ListRobot(val test: ListUiTest) {
    init {
        test.launchScreen()
    }

    abstract val maxScrolls: Int

    protected fun clickItemWithText(text: String) {
        var scrollAttempts = 0
        var clickSuccessful = false
        while (scrollAttempts < maxScrolls) {
            try {
                onView(
                    withText(
                        text
                    )
                ).perform(
                    click()
                )

                clickSuccessful = true
            } catch (ex: NoMatchingViewException) {
                scrollAttempts = onClickFailed(scrollAttempts, ex)
            } catch (ex: PerformException) {
                scrollAttempts = onClickFailed(scrollAttempts, ex)
            }

            if (clickSuccessful) {
                break
            }
        }

        if (!clickSuccessful) {
            throw IllegalStateException("View with text \"$text\" not found.")
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

    private fun onClickFailed(
        scrollAttempts: Int,
        ex: Exception
    ): Int {
        var scrollAttempts1 = scrollAttempts
        scrollAttempts1++
        Timber.e("Error: ${ex.javaClass.simpleName}. Scrolling down for ${scrollAttempts1}th time")
        scrollDown()
        return scrollAttempts1
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

    private fun scrollDown() {
        onView(
            withId(R.id.view_scroll_target)
        ).perform(
            swipeUp()
        )
    }
}
