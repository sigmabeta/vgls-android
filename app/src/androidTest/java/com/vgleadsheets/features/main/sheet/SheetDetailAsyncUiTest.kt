package com.vgleadsheets.features.main.sheet

import androidx.test.espresso.intent.rule.IntentsTestRule
import com.vgleadsheets.features.main.ListUiTest
import com.vgleadsheets.features.main.composer.composer
import com.vgleadsheets.features.main.game.game
import com.vgleadsheets.features.main.hud.HudFragment
import com.vgleadsheets.features.main.songs.songList
import com.vgleadsheets.features.main.tag.value.song.tagValueSongList
import com.vgleadsheets.features.main.viewer.viewer
import com.vgleadsheets.main.MainActivity
import org.junit.Rule
import org.junit.Test

class SheetDetailAsyncUiTest : ListUiTest() {
    override val screenId = HudFragment.TOP_LEVEL_SCREEN_ID_SONG

    @get:Rule
    override val activityRule = IntentsTestRule(
        MainActivity::class.java,
        false,
        false
    )

    @Test
    fun clickingYoutubeLaunchesSearch() {
        songList(this) {
            clickSheetWithTitle(TITLE_SECOND_ITEM)
        }

        viewer(this) {
            checkPageVisible(1)
            clickSheetDetails()
        }

        sheetDetail(this, TITLE_SECOND_ITEM, "1 pages") {
            clickYoutube()
            checkWebBrowserLaunchedUrl(
                "https://www.youtube.com/results?search_query=" +
                        "Justo%20-%20Ante"
            )
        }
    }

    @Test
    fun clickingViewSheetLaunchesViewer() {
        songList(this) {
            clickSheetWithTitle(TITLE_SECOND_ITEM, 2)
        }

        viewer(this) {
            checkPageVisible(1)
            clickSheetDetails()
        }

        sheetDetail(this, TITLE_SECOND_ITEM, "1 pages") {
            clickViewSheet()
        }

        viewer(this) {
            checkPageVisible(1)
        }
    }

    @Test
    fun clickingComposerLaunchesScreen() {
        songList(this) {
            clickSheetWithTitle(TITLE_SECOND_ITEM)
        }

        viewer(this) {
            checkPageVisible(1)
            clickSheetDetails()
        }

        sheetDetail(this, TITLE_SECOND_ITEM, "1 pages") {
            clickComposer()
        }

        composer(this, "Phung Donovan", "2 Sheets") {
        }
    }

    @Test
    fun clickingGameLaunchesScreen() {
        songList(this) {
            clickSheetWithTitle(TITLE_SECOND_ITEM)
        }

        viewer(this) {
            checkPageVisible(1)
            clickSheetDetails()
        }

        sheetDetail(this, TITLE_SECOND_ITEM, "1 pages") {
            clickGame()
        }

        game(this, "Justo", "3 Sheets") {
        }
    }

    @Test
    fun clickingRatingLaunchesScreen() {
        songList(this) {
            clickSheetWithTitle(TITLE_SECOND_ITEM)
        }

        viewer(this) {
            checkPageVisible(1)
            clickSheetDetails()
        }

        sheetDetail(this, TITLE_SECOND_ITEM, "1 pages") {
            clickRatingWithLabel("Iaculis", 7)
        }

        tagValueSongList(this, "Iaculis: 2", "8 Sheets") {
        }
    }

    @Test
    fun clickingLabelValueLaunchesScreen() {
        songList(this) {
            clickSheetWithTitle(TITLE_SECOND_ITEM)
        }

        viewer(this) {
            checkPageVisible(1)
            clickSheetDetails()
        }

        sheetDetail(this, TITLE_SECOND_ITEM, "1 pages") {
            clickLabelValueWithLabel("Eget", 18)
        }

        tagValueSongList(this, "Eget: A", "20 Sheets") {
        }
    }

    companion object {
        const val TITLE_SECOND_ITEM = "Ante"
    }
}
