package com.vgleadsheets.features.main.game

import com.vgleadsheets.R
import com.vgleadsheets.features.main.ListUiTest
import com.vgleadsheets.features.main.games.gameList
import com.vgleadsheets.features.main.hud.HudFragment
import com.vgleadsheets.features.main.viewer.viewer
import org.junit.Test

class GameAsyncUiTest: ListUiTest() {
    override val screenId = HudFragment.TOP_LEVEL_SCREEN_ID_GAME
    override val startingTopLevelScreenSubtitleId = R.string.subtitle_game

    @Test
    fun firstGameClickingFirstSongShowsViewerScreen() {
        gameList(this) {
            clickGameWithTitle(GAME_FIRST_TITLE)
        }

        game(this, GAME_FIRST_TITLE, GAME_FIRST_SHEET_COUNT) {
            checkFirstSongIs(GAME_FIRST_SHEET_TITLE_FIRST)
            checkFirstSongArtistIs(GAME_FIRST_SHEET_ARTIST_FIRST)
            clickSongWithTitle(GAME_FIRST_SHEET_TITLE_FIRST)
        }

        viewer {
            checkPageVisible(1)
        }
    }

    @Test
    fun firstGameClickingArbitrarySongShowsViewerScreen() {
        gameList(this) {
            clickGameWithTitle(GAME_FIRST_TITLE)
        }

        game(this, GAME_FIRST_TITLE, GAME_FIRST_SHEET_COUNT) {
            checkFirstSongIs(GAME_FIRST_SHEET_TITLE_FIRST)
            checkFirstSongArtistIs(GAME_FIRST_SHEET_ARTIST_FIRST)
            clickSongWithTitle(GAME_FIRST_SHEET_TITLE_ARBITRARY)
        }

        viewer {
            checkPageVisible(1)
        }
    }

    @Test
    fun arbitraryGameClickingFirstSongShowsViewerScreen() {
        gameList(this) {
            clickGameWithTitle(GAME_ARBITRARY_TITLE)
        }

        game(this, GAME_ARBITRARY_TITLE, GAME_ARBITRARY_SHEET_COUNT) {
            checkFirstSongIs(GAME_ARBITRARY_SHEET_TITLE_FIRST)
            checkFirstSongArtistIs(GAME_ARBITRARY_SHEET_ARTIST_FIRST)
            clickSongWithTitle(GAME_ARBITRARY_SHEET_TITLE_FIRST)
        }

        viewer {
            checkPageVisible(1)
        }
    }

    @Test
    fun arbitraryGameClickingArbitrarySongShowsViewerScreen() {
        gameList(this) {
            clickGameWithTitle(GAME_ARBITRARY_TITLE)
        }

        game(this, GAME_ARBITRARY_TITLE, GAME_ARBITRARY_SHEET_COUNT) {
            checkFirstSongIs(GAME_ARBITRARY_SHEET_TITLE_FIRST)
            checkFirstSongArtistIs(GAME_ARBITRARY_SHEET_ARTIST_FIRST)
            clickSongWithTitle(GAME_ARBITRARY_SHEET_TITLE_ARBITRARY)
        }

        viewer {
            checkPageVisible(1)
        }
    }

    companion object {
        const val GAME_FIRST_TITLE = "A Ac"
        const val GAME_FIRST_SHEET_COUNT = "5 Sheets"

        const val GAME_ARBITRARY_TITLE = "Quisque"
        const val GAME_ARBITRARY_SHEET_COUNT = "8 Sheets"

        const val GAME_FIRST_SHEET_TITLE_FIRST = "Amet"
        const val GAME_FIRST_SHEET_ARTIST_FIRST = "Camille Wyatt"

        const val GAME_FIRST_SHEET_TITLE_ARBITRARY = "Quis Ut Finibus Hendrerit"

        const val GAME_ARBITRARY_SHEET_TITLE_FIRST = "Ac In"
        const val GAME_ARBITRARY_SHEET_ARTIST_FIRST = "Kali Browning"

        const val GAME_ARBITRARY_SHEET_TITLE_ARBITRARY = "Pharetra Justo"
    }
}
