package com.vgleadsheets.features.main.games

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.vgleadsheets.AsyncUiTest
import com.vgleadsheets.MockStorage
import com.vgleadsheets.R
import com.vgleadsheets.RecyclerViewMatcher
import com.vgleadsheets.features.main.hud.HudFragment
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Test

class GameListAsyncUiTest : AsyncUiTest() {
    @Before
    override fun setup() {
        super.setup()
        (storage as MockStorage).savedTopLevelScreen = HudFragment.TOP_LEVEL_SCREEN_ID_GAME
    }

    @Test
    fun firstItemIsCorrect() {
        launchScreen()

        checkViewVisible(R.id.list_games)

        emitDataFromApi()

        waitForUi()

        checkFirstItemIsCorrect()
    }
    @Test
    fun noDataShowsEmptyState() {
        setApiToReturnBlank()

        launchScreen()

        emitDataFromApi()

        waitForUi()

        checkFirstItemIsEmptyState()
    }

    private fun checkFirstItemIsEmptyState() {
        checkFirstContentItem(
            hasDescendant(
                allOf(
                    instanceOf(TextView::class.java),
                    withText("No games found at all. Check your internet connection?")
                )
            )
        )
    }

    private fun checkFirstItemIsLoading() {
        checkFirstContentItem(
            hasDescendant(
                allOf(
                    withId(R.id.text_name),
                    instanceOf(CardView::class.java)
                )
            )
        )
    }

    private fun checkFirstItemIsCorrect() {
        checkFirstContentItem(
            hasDescendant(
                allOf(
                    withId(R.id.text_name),
                    withText("Consectetur")
                )
            )
        )
    }

    private fun checkFirstContentItem(
        matcher: Matcher<View>?
    ) {
        val contentView = RecyclerViewMatcher(R.id.list_games)

        onView(
            contentView.atPosition(1)
        ).check(
            matches(
                matcher
            )
        )
    }
}
