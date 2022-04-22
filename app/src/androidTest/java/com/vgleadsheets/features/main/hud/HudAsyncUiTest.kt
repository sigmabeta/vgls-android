package com.vgleadsheets.features.main.hud

import com.vgleadsheets.AsyncUiTest
import com.vgleadsheets.R
import org.junit.Test

class HudAsyncUiTest : AsyncUiTest() {
    override val startingTopLevelScreenTitleId: Int? = null

    @Test
    fun updateDateSuccessfullyUpdates() {
        launchScreen()

        hud(this) {
            checkViewVisible(R.id.button_menu)
            checkViewText("Updated Never")
        }

        updateTimeEmitTrigger.onNext(1L)

        hud(this) {
            clickView(R.id.button_menu)
            checkViewText("Updated Apr 1, 2017")
        }
    }

    @Test
    fun updateDateShowsNever() {
        launchScreen()

        hud(this) {
            clickView(R.id.button_menu)
            checkViewText( "Updated Never")
        }
    }
}
