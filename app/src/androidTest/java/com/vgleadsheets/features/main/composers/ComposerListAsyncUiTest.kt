package com.vgleadsheets.features.main.composers

import com.vgleadsheets.features.main.ListUiTest
import com.vgleadsheets.features.main.hud.HudFragment
import org.junit.Test

class ComposerListAsyncUiTest : ListUiTest() {
    override val screenId = HudFragment.TOP_LEVEL_SCREEN_ID_COMPOSER
    override val startingTopLevelScreenSubtitleId = R.string.subtitle_composer

    @Test
    fun showEmptyStateIfNoData() {
        setApiToReturnBlank()

        composerList(this) {
            checkIsEmptyStateDisplayed(LABEL_EMPTY_STATE)
        }
    }

    @Test
    fun showsDataOnSuccessfulLoad() {
        composerList(this) {
            checkFirstComposerIs(TITLE_FIRST_ITEM)
        }
    }

    @Test
    fun clickingFirstItemLoadsComposerScreen() {
        composerList(this) {
            clickItemWithTitle(TITLE_FIRST_ITEM)
        }
    }

    companion object {
        const val LABEL_EMPTY_STATE = "composers"
        const val TITLE_FIRST_ITEM = "Alona Dunn"
    }
}
