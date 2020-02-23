package com.vgleadsheets.features.main.composers

import com.vgleadsheets.features.main.ListUiTest
import com.vgleadsheets.features.main.hud.HudFragment

class ComposerListAsyncUiTest : ListUiTest() {
    override val screenId = HudFragment.TOP_LEVEL_SCREEN_ID_COMPOSER
    override val startingTopLevelScreenSubtitleId = R.string.subtitle_composer
    override val emptyStateLabel = "composers"
    override val firstItemTitle = "Alona Dunn"
    override val firstItemSubtitle = "1 Sheets"
}
