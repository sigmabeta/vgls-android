package com.vgleadsheets.features.main.games

import com.vgleadsheets.R
import com.vgleadsheets.features.main.ListUiTest
import com.vgleadsheets.features.main.hud.HudFragment

class GameListAsyncUiTest : ListUiTest() {
    override val screenId = HudFragment.TOP_LEVEL_SCREEN_ID_GAME
    override val startingTopLevelScreenSubtitleId = R.string.subtitle_game
    override val emptyStateLabel = "games"
    override val firstItemTitle = "A Ac"
    override val firstItemSubtitle = "5 Sheets"
}
