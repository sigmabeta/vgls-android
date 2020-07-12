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

            isHeaderWithTitleDisplayed(SECTION_HEADER_SONGS, SCROLL_POS_HEADER_SONG)
            isItemWithTitleDisplayed(RESULT_TITLE_SONG, SCROLL_POS_RESULT_SONG)

            isHeaderWithTitleDisplayed(SECTION_HEADER_GAMES, SCROLL_POS_HEADER_GAME)
            isItemWithTitleDisplayed(RESULT_TITLE_GAME, SCROLL_POS_RESULT_GAME)

            isHeaderWithTitleDisplayed(SECTION_HEADER_COMPOSERS, SCROLL_POS_HEADER_COMPOSER)
            isItemWithTitleDisplayed(RESULT_TITLE_COMPOSER, SCROLL_POS_RESULT_COMPOSER)
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

            isHeaderWithTitleDisplayed(SECTION_HEADER_SONGS, SCROLL_POS_HEADER_SONG)
            clickSheetWithTitle(RESULT_TITLE_SONG, SCROLL_POS_RESULT_SONG)
        }

        viewer(this) {
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

            isHeaderWithTitleDisplayed(SECTION_HEADER_SONGS, SCROLL_POS_HEADER_SONG)

            clickGameWithTitle(RESULT_TITLE_GAME, SCROLL_POS_RESULT_GAME)
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

            isHeaderWithTitleDisplayed(SECTION_HEADER_SONGS, SCROLL_POS_HEADER_SONG)

            clickComposerWithTitle(RESULT_TITLE_COMPOSER, SCROLL_POS_RESULT_COMPOSER)
        }

        composer(this, RESULT_TITLE_COMPOSER, COMPOSER_SUBTITLE_SHEET_COUNT) {
            checkFirstSongIs(COMPOSER_SONG_TITLE_FIRST)
        }
    }

    companion object {
        const val QUERY_SEARCH = "in"

        const val SECTION_HEADER_GAMES = "Games"
        const val SECTION_HEADER_SONGS = "Songs"
        const val SECTION_HEADER_COMPOSERS = "Composers"

        const val RESULT_TITLE_SONG = "Ante Integer Quisque Blandit"
        const val RESULT_TITLE_GAME = "In Tellus Curabitur"
        const val RESULT_TITLE_COMPOSER = "Robbin Hanson"

        const val GAME_SONG_TITLE_FIRST = "Congue"
        const val GAME_SUBTITLE_SHEET_COUNT = "4 Sheets"

        const val COMPOSER_SONG_TITLE_FIRST = "Justo Urna"
        const val COMPOSER_SUBTITLE_SHEET_COUNT = "1 Sheets"

        const val SCROLL_POS_HEADER_SONG = 0
        const val SCROLL_POS_RESULT_SONG = 2
        const val SCROLL_POS_HEADER_GAME = 20
        const val SCROLL_POS_RESULT_GAME = 21
        const val SCROLL_POS_HEADER_COMPOSER = 22
        const val SCROLL_POS_RESULT_COMPOSER = 30
    }
}
