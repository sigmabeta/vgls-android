package com.vgleadsheets.features.main.songs

import com.vgleadsheets.features.main.ListUiTest
import com.vgleadsheets.features.main.hud.HudFragment

class SongListAsyncUiTest : ListUiTest() {
    override val screenId = HudFragment.TOP_LEVEL_SCREEN_ID_SONG
    override val startingTopLevelScreenSubtitleId = R.string.subtitle_all_sheets
    override val emptyStateLabel = "songs"
    override val firstItemTitle = "A Lobortis"
    override val firstItemSubtitle = "1 Sheets"

    override fun goToFirstItem() {
        launchScreen()

        emitDataFromApi()

        waitForUi()

        clickFirstItem()

        checkViewVisible(com.vgleadsheets.features.main.viewer.R.id.pager_sheets)
    }
}
