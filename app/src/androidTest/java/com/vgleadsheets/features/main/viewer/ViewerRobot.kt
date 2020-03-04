package com.vgleadsheets.features.main.viewer

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription

class ViewerRobot {

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
}

fun viewer(func: ViewerRobot.() -> Unit) = ViewerRobot().apply {
    func()
}

