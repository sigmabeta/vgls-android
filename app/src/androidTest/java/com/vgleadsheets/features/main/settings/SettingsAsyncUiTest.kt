package com.vgleadsheets.features.main.settings

import com.vgleadsheets.R
import com.vgleadsheets.features.main.ListUiTest
import com.vgleadsheets.features.main.hud.HudFragment
import com.vgleadsheets.features.main.hud.hud
import org.junit.Test

class SettingsAsyncUiTest : ListUiTest() {
    override val screenId = HudFragment.TOP_LEVEL_SCREEN_ID_SONG

    override fun setup() {
        super.setup()
        launchSettingsScreen()
    }

    @Test
    fun toggleSettingSheetsScreenOn() {
        settings(this) {
            checkBooleanSettingValueIs(R.string.label_setting_screen_on, false, 1)

            clickSettingWithTitle(R.string.label_setting_screen_on, 1)
            checkBooleanSettingValueIs(R.string.label_setting_screen_on, true, 1)

            clickSettingWithTitle(R.string.label_setting_screen_on, 1)
            checkBooleanSettingValueIs(R.string.label_setting_screen_on, false, 1)
        }
    }

    private fun launchSettingsScreen() {
        hud(this) {
            checkSearchButtonIsHamburger()

            clickView(R.id.button_menu)
            checkViewVisible(R.id.text_update_time)

            clickView(R.id.layout_settings)
            checkViewNotVisible(R.id.text_update_time)
        }
    }
}