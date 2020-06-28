package com.vgleadsheets.features.main.jams

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.vgleadsheets.features.main.ListRobot
import com.vgleadsheets.features.main.ListUiTest
import org.hamcrest.Matchers.allOf

class JamListRobot(test: ListUiTest) : ListRobot(test) {
    init {
        checkScreenHeader("Jam Sessions", "")
    }

    fun openFindJamDialog() {
        clickCtaWithTitle(resources.getString(R.string.cta_find_jam), 1)
    }

    fun typeInSearchBox(text: String) {
        onView(
            withId(R.id.edit_jam_name)
        ).perform(
            typeText(
                text
            )
        )
    }

    fun clickSearchButton() {
        onView(
            withId(R.id.edit_jam_name)
        ).perform(
            pressImeActionButton()
        )
    }

    fun checkIsEmptyStateDisplayed() {
        onView(
            allOf(
                withId(R.id.component_empty_state),
                hasDescendant(
                    withText("You haven't followed any jams. Click above to search for one.")
                )
            )
        ).check(
            matches(
                isDisplayed()
            )
        )
    }
}

fun jamList(
    test: ListUiTest,
    func: JamListRobot.() -> Unit
) = JamListRobot(test).apply {
    func()
}
