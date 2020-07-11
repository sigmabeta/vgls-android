package com.vgleadsheets.features.main.composer

import com.vgleadsheets.features.main.ListUiTest
import com.vgleadsheets.features.main.composers.composerList
import com.vgleadsheets.features.main.hud.HudFragment
import com.vgleadsheets.features.main.viewer.viewer
import org.junit.Test

class ComposerAsyncUiTest : ListUiTest() {
    override val screenId = HudFragment.TOP_LEVEL_SCREEN_ID_COMPOSER

    @Test
    fun firstComposerClickingFirstSongShowsViewerScreen() {
        composerList(this) {
            clickComposerWithTitle(COMPOSER_FIRST_TITLE)
        }

        composer(this, COMPOSER_FIRST_TITLE, COMPOSER_FIRST_SHEET_COUNT) {
            checkFirstSongIs(COMPOSER_FIRST_SHEET_TITLE_FIRST)
            checkFirstSongGameIs(COMPOSER_FIRST_SHEET_GAME_FIRST)
            clickSheetWithTitle(COMPOSER_FIRST_SHEET_TITLE_FIRST)
        }

        viewer (this) {
            checkPageVisible(1)
        }
    }

    @Test
    fun firstComposerClickingArbitrarySongShowsViewerScreen() {
        composerList(this) {
            clickComposerWithTitle(COMPOSER_FIRST_TITLE)
        }

        composer(this, COMPOSER_FIRST_TITLE, COMPOSER_FIRST_SHEET_COUNT) {
            checkFirstSongIs(COMPOSER_FIRST_SHEET_TITLE_FIRST)
            checkFirstSongGameIs(COMPOSER_FIRST_SHEET_GAME_FIRST)
            clickSheetWithTitle(COMPOSER_FIRST_SHEET_TITLE_ARBITRARY)
        }
    }

    @Test
    fun arbitraryComposerClickingFirstSongShowsViewerScreen() {
        composerList(this) {
            clickComposerWithTitle(COMPOSER_ARBITRARY_TITLE, COMPOSER_ARBITRARY_SCROLL_POS)
        }

        composer(this, COMPOSER_ARBITRARY_TITLE, COMPOSER_ARBITRARY_SHEET_COUNT) {
            checkFirstSongIs(COMPOSER_ARBITRARY_SHEET_TITLE_FIRST)
            checkFirstSongGameIs(COMPOSER_ARBITRARY_SHEET_GAME_FIRST)
            clickSheetWithTitle(COMPOSER_ARBITRARY_SHEET_TITLE_FIRST)
        }

        viewer (this) {
            checkPageVisible(1)
        }
    }

    @Test
    fun arbitraryComposerClickingArbitrarySongShowsViewerScreen() {
        composerList(this) {
            clickComposerWithTitle(COMPOSER_ARBITRARY_TITLE, COMPOSER_ARBITRARY_SCROLL_POS)
        }

        composer(this, COMPOSER_ARBITRARY_TITLE, COMPOSER_ARBITRARY_SHEET_COUNT) {
            checkFirstSongIs(COMPOSER_ARBITRARY_SHEET_TITLE_FIRST)
            checkFirstSongGameIs(COMPOSER_ARBITRARY_SHEET_GAME_FIRST)
            clickSheetWithTitle(COMPOSER_ARBITRARY_SHEET_TITLE_ARBITRARY)
        }

        viewer (this) {
            checkPageVisible(1)
        }
    }

    companion object {
        const val COMPOSER_FIRST_TITLE = "Albertine Rice"
        const val COMPOSER_FIRST_SHEET_COUNT = "2 Sheets"

        const val COMPOSER_ARBITRARY_SCROLL_POS = 45
        const val COMPOSER_ARBITRARY_TITLE = "Odessa Maynard"
        const val COMPOSER_ARBITRARY_SHEET_COUNT = "2 Sheets"

        const val COMPOSER_FIRST_SHEET_TITLE_FIRST = "Iaculis Hendrerit Ultricies"
        const val COMPOSER_FIRST_SHEET_GAME_FIRST = "Ipsum"

        const val COMPOSER_FIRST_SHEET_TITLE_ARBITRARY = "Suspendisse Nam Quis"

        const val COMPOSER_ARBITRARY_SHEET_TITLE_FIRST = "Finibus Aliquam Neque Quisque"
        const val COMPOSER_ARBITRARY_SHEET_GAME_FIRST = "Justo"

        const val COMPOSER_ARBITRARY_SHEET_TITLE_ARBITRARY = "In Hendrerit Quisque Est"
    }
}
