package com.vgleadsheets.features.main.game

import com.vgleadsheets.features.main.ListUiTest
import com.vgleadsheets.features.main.games.gameList
import com.vgleadsheets.features.main.hud.HudFragment
import com.vgleadsheets.features.main.viewer.viewer
import org.junit.Test

class GameAsyncUiTest : ListUiTest() {
    override val screenId = HudFragment.TOP_LEVEL_SCREEN_ID_GAME

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
            clickGameWithTitle(GAME_ARBITRARY_TITLE, GAME_ARBITRARY_SCROLL_POS)
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
            clickGameWithTitle(GAME_ARBITRARY_TITLE, GAME_ARBITRARY_SCROLL_POS)
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
        const val GAME_FIRST_TITLE = "Elit"
        const val GAME_FIRST_SHEET_COUNT = "5 Sheets"

        const val GAME_ARBITRARY_SCROLL_POS = 7
        const val GAME_ARBITRARY_TITLE = "Pellentesque Fermentum Donec"
        const val GAME_ARBITRARY_SHEET_COUNT = "5 Sheets"

        const val GAME_FIRST_SHEET_TITLE_FIRST = "Et Pellentesque Adipiscing Ligula"
        const val GAME_FIRST_SHEET_ARTIST_FIRST = "Jeannetta Nixon"

        const val GAME_FIRST_SHEET_TITLE_ARBITRARY = "In Hendrerit Quisque Est"

        const val GAME_ARBITRARY_SHEET_TITLE_FIRST = "Hendrerit Lobortis Dolor"
        const val GAME_ARBITRARY_SHEET_ARTIST_FIRST = "Lacresha Dunn"

        const val GAME_ARBITRARY_SHEET_TITLE_ARBITRARY = "Justo Neque Finibus In"
    }
}
