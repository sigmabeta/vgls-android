package com.vgleadsheets.features.main.games

import com.vgleadsheets.features.main.ListUiTest
import com.vgleadsheets.features.main.game.game
import com.vgleadsheets.features.main.hud.HudFragment
import org.junit.Test

class GameListAsyncUiTest : ListUiTest() {
    override val screenId = HudFragment.TOP_LEVEL_SCREEN_ID_GAME

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
            checkFirstGameIs(TITLE_FIRST_GAME)
        }
    }

    @Test
    fun clickingFirstItemLoadsGameScreen() {
        gameList(this) {
            clickGameWithTitle(TITLE_FIRST_GAME)
        }

        game(this, TITLE_FIRST_GAME, GAME_FIRST_SHEET_COUNT) { }
    }

    @Test
    fun clickingArbitraryItemLoadsGameScreen() {
        gameList(this) {
            clickGameWithTitle(TITLE_ARBITRARY_GAME, GAME_ARBITRARY_SCROLL_POS)
        }

        game(this, TITLE_ARBITRARY_GAME, GAME_ARBITRARY_SHEET_COUNT) { }
    }

    companion object {
        const val LABEL_EMPTY_STATE = "games"

        const val TITLE_FIRST_GAME = "Elit"
        const val TITLE_ARBITRARY_GAME = "Ultricies Justo"

        const val GAME_FIRST_SHEET_COUNT = "5 Sheets"

        const val GAME_ARBITRARY_SHEET_COUNT = "3 Sheets"

        const val GAME_ARBITRARY_SCROLL_POS = 8
    }
}
