package com.vgleadsheets.features.main.games

import com.vgleadsheets.R
import com.vgleadsheets.features.main.ListUiTest
import com.vgleadsheets.features.main.hud.HudFragment
import org.junit.Test

class GameListAsyncUiTest : ListUiTest() {
    override val screenId = HudFragment.TOP_LEVEL_SCREEN_ID_GAME
    override val startingTopLevelScreenSubtitleId = R.string.subtitle_game

    @Test
    fun showEmptyStateIfNoData() {
        setApiToReturnBlank()

        gameList(this) {
            checkIsEmptyStateDisplayed(LABEL_EMPTY_STATE)
        }
    }

    @Test
    fun showsDataOnSuccessfulLoad() {
        gameList(this) {
            checkFirstGameIs(TITLE_FIRST_ITEM)
        }
    }

    @Test
    fun clickingFirstItemLoadsGameScreen() {
        gameList(this) {
            clickGameWithTitle(TITLE_FIRST_ITEM)
        }
    }

    companion object {
        const val LABEL_EMPTY_STATE = "games"
        const val TITLE_FIRST_ITEM = "A Ac"
    }
}
