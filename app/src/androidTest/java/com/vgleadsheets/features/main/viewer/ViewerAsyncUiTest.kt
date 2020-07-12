package com.vgleadsheets.features.main.viewer

import androidx.test.espresso.intent.rule.IntentsTestRule
import com.vgleadsheets.features.main.ListUiTest
import com.vgleadsheets.features.main.hud.HudFragment
import com.vgleadsheets.features.main.sheet.sheetDetail
import com.vgleadsheets.features.main.songs.songList
import com.vgleadsheets.main.MainActivity
import org.junit.Rule
import org.junit.Test

class ViewerAsyncUiTest  : ListUiTest() {
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
            clickSheetWithTitle(TITLE_FIRST_ITEM)
        }

        viewer (this) {
            checkPageVisible(1)
            clickYoutube()
            checkWebBrowserLaunchedUrl("https://www.youtube.com/results?search_query=" +
                    "Ultricies%20Ligula%20Ultricies%20Ultricies%20-%20Adipiscing%20In%20Orci")
        }
    }

    @Test
    fun clickingSheetDetailLaunchesScreen() {
        songList(this) {
            clickSheetWithTitle(TITLE_FIRST_ITEM)
        }

        viewer (this) {
            checkPageVisible(1)
            clickSheetDetails()
        }

        sheetDetail(this, TITLE_FIRST_ITEM, "2 pages") {

        }
    }

    companion object {
        const val TITLE_FIRST_ITEM = "Adipiscing In Orci"
    }
}
