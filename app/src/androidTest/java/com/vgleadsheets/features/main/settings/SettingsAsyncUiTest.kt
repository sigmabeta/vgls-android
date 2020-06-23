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
            checkBooleanSettingValueIs("Sheets keep screen on", false, 1)

            clickCheckboxWithTitle("Sheets keep screen on", 1)
            checkBooleanSettingValueIs("Sheets keep screen on", true, 1)

            clickCheckboxWithTitle("Sheets keep screen on", 1)
            checkBooleanSettingValueIs("Sheets keep screen on", false, 1)
        }
    }

    @Test
    fun clickAboutLaunchesAboutScreen() {
        settings(this) {
            clickLinkWithTitle("About this app", 3)
        }

        about(this) { }
    }

    @Test
    fun clickAboutAndOpenSourceLaunchesLicenseScreen() {
        settings(this) {
            clickLinkWithTitle("About this app", 3)
        }

        about(this) {
            clickLinkWithTitle("Open-source libraries used", 4)
            checkLicenseViewIsVisible()
        }
    }

    @Test
    fun clickAboutAndVglsLaunchesWebBrowser() {
        settings(this) {
            clickLinkWithTitle("About this app", 3)
        }

        about(this) {
            clickTwoLineLinkWithTitle("VGLeadSheets.com", 2)
            checkWebBrowserLaunchedUrl("https://www.vgleadsheets.com/")
        }
    }

    @Test
    fun clickAboutAndGiantBombLaunchesWebBrowser() {
        settings(this) {
            clickLinkWithTitle("About this app")
        }

        about(this) {
            clickTwoLineLinkWithTitle("Giant Bomb Wiki", 3)
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