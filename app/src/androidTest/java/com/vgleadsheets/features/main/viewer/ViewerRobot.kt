package com.vgleadsheets.features.main.viewer

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import com.vgleadsheets.features.main.ListRobot
import com.vgleadsheets.features.main.ListUiTest

class ViewerRobot(test: ListUiTest) : ListRobot(test) {

    fun nextPage() {
        swipeLeft()
    }

    fun checkPageVisible(pageNumber: Int) {
        onView(
            withContentDescription(
                "file:///android_asset/sheetsC/goose-$pageNumber.png"
            )
        ).check(
            matches(
                isCompletelyDisplayed()
            )
        )
    }

    fun clickSheetDetails() {
        clickToolbarItemWithTitle("See details for this sheet")
    }

    fun clickYoutube() {
        clickToolbarItemWithTitle("Search Youtube for this song")
    }
}

fun viewer(
    test: ListUiTest,
    func: ViewerRobot.() -> Unit
) = ViewerRobot(test).apply {
    func()
}
