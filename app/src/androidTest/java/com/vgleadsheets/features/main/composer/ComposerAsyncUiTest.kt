package com.vgleadsheets.features.main.composer

import com.vgleadsheets.R
import com.vgleadsheets.features.main.ListUiTest
import com.vgleadsheets.features.main.composers.composerList
import com.vgleadsheets.features.main.hud.HudFragment
import com.vgleadsheets.features.main.viewer.viewer
import org.junit.Test

class ComposerAsyncUiTest : ListUiTest() {
    override val screenId = HudFragment.TOP_LEVEL_SCREEN_ID_COMPOSER
    override val startingTopLevelScreenSubtitleId = R.string.subtitle_composer

    @Test
    fun firstComposerClickingFirstSongShowsViewerScreen() {
        composerList(this) {
            clickComposerWithTitle(COMPOSER_FIRST_TITLE)
        }

        composer(this, COMPOSER_FIRST_TITLE, COMPOSER_FIRST_SHEET_COUNT) {
            checkFirstSongIs(COMPOSER_FIRST_SHEET_TITLE_FIRST)
            checkFirstSongGameIs(COMPOSER_FIRST_SHEET_GAME_FIRST)
            clickSongWithTitle(COMPOSER_FIRST_SHEET_TITLE_FIRST)
        }

        viewer {
            checkPageVisible(1)
        }
    }

    // TODO Can't test with the current dataset because the first composer has only one song.
    /*
    @Test
    fun firstComposerClickingArbitrarySongShowsViewerScreen() {
        composerList(this) {
            clickComposerWithTitle(COMPOSER_FIRST_TITLE)
        }

        composer(this, COMPOSER_FIRST_TITLE, COMPOSER_FIRST_SHEET_COUNT) {
            checkFirstSongIs(COMPOSER_FIRST_SHEET_TITLE_FIRST)
            checkFirstSongGameIs(COMPOSER_FIRST_SHEET_GAME_FIRST)
            clickSongWithTitle(COMPOSER_FIRST_SHEET_TITLE_ARBITRARY)
        }
    }
    */

    @Test
    fun arbitraryComposerClickingFirstSongShowsViewerScreen() {
        composerList(this) {
            clickComposerWithTitle(COMPOSER_ARBITRARY_TITLE)
        }

        composer(this, COMPOSER_ARBITRARY_TITLE, COMPOSER_ARBITRARY_SHEET_COUNT) {
            checkFirstSongIs(COMPOSER_ARBITRARY_SHEET_TITLE_FIRST)
            checkFirstSongGameIs(COMPOSER_ARBITRARY_SHEET_GAME_FIRST)
            clickSongWithTitle(COMPOSER_ARBITRARY_SHEET_TITLE_FIRST)
        }

        viewer {
            checkPageVisible(1)
        }
    }

    @Test
    fun arbitraryComposerClickingArbitrarySongShowsViewerScreen() {
        composerList(this) {
            clickComposerWithTitle(COMPOSER_ARBITRARY_TITLE)
        }

        composer(this, COMPOSER_ARBITRARY_TITLE, COMPOSER_ARBITRARY_SHEET_COUNT) {
            checkFirstSongIs(COMPOSER_ARBITRARY_SHEET_TITLE_FIRST)
            checkFirstSongGameIs(COMPOSER_ARBITRARY_SHEET_GAME_FIRST)
            clickSongWithTitle(COMPOSER_ARBITRARY_SHEET_TITLE_ARBITRARY)
        }

        viewer {
            checkPageVisible(1)
        }
    }

    companion object {
        const val COMPOSER_FIRST_TITLE = "Alona Dunn"
        const val COMPOSER_FIRST_SHEET_COUNT = "1 Sheets"

        const val COMPOSER_ARBITRARY_TITLE = "Epifania Allen"
        const val COMPOSER_ARBITRARY_SHEET_COUNT = "4 Sheets"

        const val COMPOSER_FIRST_SHEET_TITLE_FIRST = "Integer Fermentum"
        const val COMPOSER_FIRST_SHEET_GAME_FIRST = "Ultricies Vel Nam Suspendisse"

        // TODO Can't test with the current dataset because the first composer has only one song.
        // const val COMPOSER_FIRST_SHEET_TITLE_ARBITRARY = "Quis Ut Finibus Hendrerit"

        const val COMPOSER_ARBITRARY_SHEET_TITLE_FIRST = "Elit"
        const val COMPOSER_ARBITRARY_SHEET_GAME_FIRST = "Quisque"

        const val COMPOSER_ARBITRARY_SHEET_TITLE_ARBITRARY = "Velit Tincidunt Dictum Curabitur"
    }
}
