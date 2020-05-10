package com.vgleadsheets.features.main.search

import com.vgleadsheets.features.main.ListUiTest
import com.vgleadsheets.features.main.composer.composer
import com.vgleadsheets.features.main.game.game
import com.vgleadsheets.features.main.hud.HudFragment
import com.vgleadsheets.features.main.hud.hud
import com.vgleadsheets.features.main.viewer.viewer
import org.junit.Test

class SearchAsyncUiTest : ListUiTest() {
    override val screenId = HudFragment.TOP_LEVEL_SCREEN_ID_COMPOSER

    @Test
    fun searchLaunchesSuccessfully() {
        hud(this) {
            clickSearch()
        }

        search(this) { }
    }

    @Test
    fun searchShowsValidData() {
        hud(this) {
            clickSearch()
        }

        search(this) {
            typeInSearchBox(QUERY_SEARCH)

            waitForUi()
            waitForUi()

            isItemWithTextDisplayed("Quis Donec")

            isItemWithTextDisplayed("Games")
            isItemWithTextDisplayed("Quisque")

            isItemWithTextDisplayed("Composers")
            isItemWithTextDisplayed("Cinda Quinn")
        }
    }

    @Test
    fun searchLaunchesSongSuccessfully() {
        hud(this) {
            clickSearch()
        }

        search(this) {
            typeInSearchBox(QUERY_SEARCH)

            waitForUi()
            waitForUi()

            clickResultWithText(RESULT_TITLE_SONG)
        }

        viewer {
            checkPageVisible(1)
        }
    }

    @Test
    fun searchLaunchesGameSuccessfully() {
        hud(this) {
            clickSearch()
        }

        search(this) {
            typeInSearchBox(QUERY_SEARCH)

            waitForUi()
            waitForUi()

            clickResultWithText(RESULT_TITLE_GAME)
        }

        game(this, RESULT_TITLE_GAME, GAME_SUBTITLE_SHEET_COUNT) {
            checkFirstSongIs(GAME_SONG_TITLE_FIRST)
        }
    }

    @Test
    fun searchLaunchesComposerSuccessfully() {
        hud(this) {
            clickSearch()
        }

        search(this) {
            typeInSearchBox(QUERY_SEARCH)

            waitForUi()
            waitForUi()

            clickResultWithText(RESULT_TITLE_COMPOSER)
        }

        composer(this, RESULT_TITLE_COMPOSER, COMPOSER_SUBTITLE_SHEET_COUNT) {
            checkFirstSongIs(COMPOSER_SONG_TITLE_FIRST)
        }
    }

    companion object {
        const val QUERY_SEARCH = "qui"

        const val RESULT_TITLE_SONG = "Quis Donec"
        const val RESULT_TITLE_GAME = "Quisque"
        const val RESULT_TITLE_COMPOSER = "Cinda Quinn"

        const val GAME_SONG_TITLE_FIRST = "Ac In"
        const val GAME_SUBTITLE_SHEET_COUNT= "8 Sheets"

        const val COMPOSER_SONG_TITLE_FIRST = "Pellentesque Quis Finibus"
        const val COMPOSER_SUBTITLE_SHEET_COUNT= "1 Sheets"
    }
}
