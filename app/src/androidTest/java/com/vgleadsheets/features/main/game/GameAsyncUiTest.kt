package com.vgleadsheets.features.main.game

import com.vgleadsheets.R
import com.vgleadsheets.features.main.ListUiTest
import com.vgleadsheets.features.main.games.gameList
import com.vgleadsheets.features.main.hud.HudFragment
import org.junit.Test

class GameAsyncUiTest: ListUiTest() {
    override val screenId = HudFragment.TOP_LEVEL_SCREEN_ID_GAME
    override val startingTopLevelScreenSubtitleId = R.string.subtitle_game

    @Test
    fun clickingFirstItemLoadsGameScreen() {
        gameList(this) {
            clickGameWithTitle(GAME_TITLE_FIRST_ITEM)
        }

        game(this, GAME_TITLE_FIRST_ITEM, GAME_SHEET_COUNT_FIRST_ITEM) {
            checkFirstSongIs(SHEET_TITLE_FIRST_ITEM)
        }
    }

    companion object {
        const val GAME_TITLE_FIRST_ITEM = "A Ac"
        const val GAME_SHEET_COUNT_FIRST_ITEM = "5 Sheets"

        const val SHEET_TITLE_FIRST_ITEM = "Amet"
        const val SHEET_ARTIST_FIRST_ITEM = "Camille Wyatt"
    }
}
