package com.vgleadsheets.features.main.hud

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.vgleadsheets.R
import com.vgleadsheets.UiTest
import org.hamcrest.Matchers.allOf
import org.junit.Test

class HudUiTest: UiTest() {
    @Test
    fun openAndCloseMenu() {
        launchScreen()

        clickView(R.id.button_menu)
        checkViewVisible(R.id.text_update_time)

        clickView(R.id.button_menu)
        checkViewNotVisible(R.id.text_update_time)
    }

    @Test
    fun navigateToComposerList() {
        launchScreen()

        checkNavigationButton(
            R.id.layout_by_composer,
            R.string.app_name,
            R.string.subtitle_composer
        )
    }

    @Test
    fun navigateToTagList() {
        launchScreen()

        checkNavigationButton(
            R.id.layout_by_tag,
            R.string.app_name,
            R.string.subtitle_tags
        )
    }

    @Test
    fun navigateToSongList() {
        launchScreen()

        checkNavigationButton(
            R.id.layout_all_sheets,
            R.string.app_name,
            R.string.subtitle_all_sheets
        )
    }

    @Test
    fun navigateToJamList() {
        launchScreen()

        checkNavigationButton(
            R.id.layout_jams,
            R.string.title_jams,
            null
        )
    }

    @Test
    fun navigateToSettings() {
        launchScreen()

        clickView(R.id.button_menu)
        checkViewVisible(R.id.text_update_time)

        clickView(R.id.layout_settings)
        checkViewNotVisible(R.id.text_update_time)

        checkViewVisible(R.id.checkbox_setting)
    }

    @Test
    fun navigateToDebugSettings() {
        launchScreen()

        clickView(R.id.button_menu)
        checkViewVisible(R.id.text_update_time)

        clickView(R.id.layout_debug)
        checkViewNotVisible(R.id.text_update_time)

        onView(
            allOf(
                withId(R.id.text_name),
                withText(R.string.label_debug_network_endpoint)
            )
        ).check(
            matches(
                isDisplayed()
            )
        )
    }

    private fun checkNavigationButton(
        buttonId: Int,
        titleStringId: Int,
        subtitleStringId: Int?
    ) {
        clickView(R.id.button_menu)
        checkViewVisible(R.id.text_update_time)

        clickView(buttonId)
        checkViewNotVisible(R.id.text_update_time)

        checkTopLevelScreen(titleStringId, subtitleStringId)
    }

    private fun checkTopLevelScreen(titleStringId: Int, subtitleStringId: Int?) {
        checkViewVisible(R.id.text_title_title)
        checkViewVisible(R.id.text_title_subtitle)

        checkViewText(R.id.text_title_title, titleStringId)

        if (subtitleStringId != null) {
            checkViewText(R.id.text_title_subtitle, subtitleStringId)
        }
    }
}
