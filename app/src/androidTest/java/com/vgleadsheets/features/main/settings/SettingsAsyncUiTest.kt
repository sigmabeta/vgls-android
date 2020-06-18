package com.vgleadsheets.features.main.settings

import androidx.test.espresso.intent.rule.IntentsTestRule
import com.vgleadsheets.R
import com.vgleadsheets.features.main.ListUiTest
import com.vgleadsheets.features.main.hud.HudFragment
import com.vgleadsheets.features.main.hud.hud
import com.vgleadsheets.features.main.settings.about.about
import com.vgleadsheets.main.MainActivity
import org.junit.Rule
import org.junit.Test

class SettingsAsyncUiTest : ListUiTest() {
    override val screenId = HudFragment.TOP_LEVEL_SCREEN_ID_SONG

    @get:Rule
    override val activityRule = IntentsTestRule(
        MainActivity::class.java,
        false,
        false
    )

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

    @Test
    fun clickAboutLaunchesAboutScreen() {
        settings(this) {
            clickSettingWithTitle(R.string.label_link_about)
        }

        about(this) { }
    }

    @Test
    fun clickAboutAndOpenSourceLaunchesLicenseScreen() {
        settings(this) {
            clickSettingWithTitle(R.string.label_link_about)
        }

        about(this) {
            clickSettingWithTitle(R.string.label_link_licenses)
            checkLicenseViewIsVisible()
        }
    }

    @Test
    fun clickAboutAndVglsLaunchesWebBrowser() {
        settings(this) {
            clickSettingWithTitle(R.string.label_link_about)
        }

        about(this) {
            clickSettingWithTitle(R.string.label_link_vgls)
            checkWebBrowserLaunchedUrl("https://www.vgleadsheets.com/")
        }
    }

    @Test
    fun clickAboutAndGiantBombLaunchesWebBrowser() {
        settings(this) {
            clickSettingWithTitle(R.string.label_link_about)
        }

        about(this) {
            clickSettingWithTitle(R.string.label_link_giantbomb)
            checkWebBrowserLaunchedUrl("https://www.giantbomb.com/")
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