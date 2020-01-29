package com.vgleadsheets.features.main.games

import androidx.cardview.widget.CardView
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.vgleadsheets.AsyncUiTest
import com.vgleadsheets.R
import com.vgleadsheets.RecyclerViewMatcher
import org.hamcrest.Matchers
import org.junit.Test

class GameListAsyncUiTest: AsyncUiTest() {
    @Test
    fun firstItemIsCorrect() {
        launchScreen()

        val contentView = RecyclerViewMatcher(R.id.list_games)

        Espresso.onView(contentView.atPosition(1))
            .check(
                ViewAssertions.matches(
                    ViewMatchers.hasDescendant(
                        Matchers.allOf(
                            Matchers.instanceOf(CardView::class.java),
                            ViewMatchers.withId(R.id.text_name)
                        )
                    )
                )
            )

        digestEmitTrigger.onNext(1L)

        Espresso.onView(contentView.atPosition(1))
            .check(
                ViewAssertions.matches(
                    ViewMatchers.hasDescendant(
                        Matchers.allOf(
                            ViewMatchers.withId(R.id.text_name),
                            ViewMatchers.withText("A")
                        )
                    )
                )
            )
    }
}
