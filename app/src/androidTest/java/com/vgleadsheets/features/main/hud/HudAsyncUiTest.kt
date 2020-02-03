package com.vgleadsheets.features.main.hud

import com.vgleadsheets.AsyncUiTest
import com.vgleadsheets.R
import org.junit.Test

class HudAsyncUiTest: AsyncUiTest() {
    @Test
    fun updateDateSuccessfullyUpdates() {
        launchScreen()

        checkViewVisible(R.id.button_menu)
        checkViewText(R.id.text_update_time, "Updated Never")

        updateTimeEmitTrigger.onNext(1L)

        clickView(R.id.button_menu)
        checkViewVisible(R.id.text_update_time)
        checkViewText(R.id.text_update_time, "Updated Apr 1, 2017")
    }

    @Test
    fun updateDateShowsNever() {
        launchScreen()

        clickView(R.id.button_menu)
        checkViewText(R.id.text_update_time, "Updated Never")
    }
}
