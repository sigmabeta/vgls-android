package com.vgleadsheets.features.main.search

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.vgleadsheets.features.main.ListRobot
import com.vgleadsheets.features.main.ListUiTest

class SearchRobot(test: ListUiTest) : ListRobot(test) {
    init {
        checkViewWithIdAndTextVisible(R.id.text_empty_state_search, R.string.search_empty_no_query)
    }

    fun typeInSearchBox(text: String) {
        onView(
            withId(R.id.edit_search_query)
        ).perform(
            typeText(
                text
            )
        )
    }
}

fun search(
    test: ListUiTest,
    func: SearchRobot.() -> Unit
) = SearchRobot(test).apply {
    func()
}
