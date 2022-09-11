package com.vgleadsheets.features.main.hud

import com.vgleadsheets.AsyncUiTest
import com.vgleadsheets.R
import org.junit.Ignore
import org.junit.Test

class HudAsyncUiTest : AsyncUiTest() {
    override val startingTopLevelScreenTitleId: Int? = null

    @Test
    @Ignore("Broken as of recycler view refactor.")
    fun updateDateSuccessfullyUpdates() {
        launchScreen()

        hud(this) {
            clickView(R.id.button_menu)
            checkViewText(R.string.label_settings)
            checkViewText("Checking for updates...")
        }

        updateTimeEmitTrigger.emit(1L)

        hud(this) {
            checkViewText(R.string.label_settings)
            checkViewText("Updated Apr 1, 2017")
        }
    }

    @Test
    fun updateDateShowsNever() {
        launchScreen()

        hud(this) {
            clickView(R.id.button_menu)
            checkViewText("Checking for updates...")
        }
    }
}
