package com.vgleadsheets.features.main.composers

import com.vgleadsheets.features.main.ListUiTest
import com.vgleadsheets.features.main.composer.composer
import com.vgleadsheets.features.main.hud.HudFragment
import org.junit.Test

class ComposerListAsyncUiTest : ListUiTest() {
    override val screenId = HudFragment.TOP_LEVEL_SCREEN_ID_COMPOSER

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
            clickComposerWithTitle(TITLE_FIRST_ITEM)
        }

        composer(this, TITLE_FIRST_ITEM, "2 Sheets") {

        }
    }

    companion object {
        const val LABEL_EMPTY_STATE = "composers"
        const val TITLE_FIRST_ITEM = "Albertine Rice"
    }
}
