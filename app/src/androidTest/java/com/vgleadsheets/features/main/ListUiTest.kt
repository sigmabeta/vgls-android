package com.vgleadsheets.features.main

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.vgleadsheets.AsyncUiTest
import com.vgleadsheets.MockStorage
import com.vgleadsheets.R
import com.vgleadsheets.RecyclerViewMatcher
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Test

abstract class ListUiTest : AsyncUiTest() {
    abstract val screenId: String
    abstract val emptyStateLabel: String
    abstract val firstItemTitle: String
    abstract val firstItemSubtitle: String

    @Before
    override fun setup() {
        super.setup()
        (storage as MockStorage).savedTopLevelScreen = screenId
    }

    @Test
    fun noDataShowsEmptyState() {
        setApiToReturnBlank()

        launchScreen()

        emitDataFromApi()

        waitForUi()

        checkFirstItemIsEmptyState()
    }

    @Test
    fun firstItemIsCorrect() {
        launchScreen()

        emitDataFromApi()

        waitForUi()

        checkFirstItemIsCorrect()
    }

    @Test
    open fun goToFirstItem() {
        launchScreen()

        emitDataFromApi()

        waitForUi()

        clickFirstItem()

        checkScreenHeader(firstItemTitle, firstItemSubtitle)
    }

    protected fun checkFirstItemIsEmptyState() {
        checkFirstContentItem(
            hasDescendant(
                allOf(
                    withId(R.id.text_empty_state_reason),
                    withText("No $emptyStateLabel found at all. Check your internet connection?")
                )
            )
        )
    }

    protected fun checkFirstItemIsCorrect() {
        checkFirstContentItem(
            hasDescendant(
                Matchers.allOf(
                    withId(R.id.text_name),
                    withText(firstItemTitle)
                )
            )
        )
    }

    protected fun clickFirstItem() {
        clickListItemAtPosition(1)
    }

    private fun clickListItemAtPosition(position: Int) {
        val contentView = RecyclerViewMatcher()

        onView(
            contentView.atPosition(position)
        ).perform(
            click()
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
