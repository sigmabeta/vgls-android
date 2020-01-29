package com.vgleadsheets.features.main.hud

import com.vgleadsheets.AsyncUiTest
import com.vgleadsheets.R
import org.junit.Test

class HudAsyncUiTest: AsyncUiTest() {
    @Test
    fun updateDateIsCorrect() {
        launchScreen()

        clickView(R.id.button_menu)

        checkViewVisible(R.id.text_update_time)
        checkViewText(R.id.text_update_time, "Updated Never")

        updateTimeEmitTrigger.onNext(1L)

        checkViewText(R.id.text_update_time, "Updated Apr 1, 2017")
    }
}
