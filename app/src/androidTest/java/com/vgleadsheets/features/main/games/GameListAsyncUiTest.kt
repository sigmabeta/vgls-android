package com.vgleadsheets.features.main.games

import androidx.cardview.widget.CardView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import com.vgleadsheets.AsyncUiTest
import com.vgleadsheets.R
import com.vgleadsheets.RecyclerViewMatcher
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.junit.Test

class GameListAsyncUiTest : AsyncUiTest() {
    @Test
    fun firstItemIsCorrect() {
        launchScreen()

        val contentView = RecyclerViewMatcher(R.id.list_games)

        onView(
            contentView.atPosition(1)
        ).check(
            matches(
                hasDescendant(
                    allOf(
                        instanceOf(CardView::class.java),
                        ViewMatchers.withId(R.id.text_name)
                    )
                )
            )
        )

        digestEmitTrigger.onNext(1L)

        onView(
            contentView.atPosition(1)
        ).check(
            matches(
                hasDescendant(
                    allOf(
                        ViewMatchers.withId(R.id.text_name),
                        ViewMatchers.withText("A")
                    )
                )
            )
        )
    }
}
