package com.vgleadsheets.features.main.hud

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.vgleadsheets.MockStorage
import com.vgleadsheets.R
import com.vgleadsheets.UiTest
import org.hamcrest.Matchers.allOf
import org.junit.Test

class HudUiTest : UiTest() {
    override val startingTopLevelScreenTitleId: Int? = null
    override val startingTopLevelScreenSubtitleId = 0

    @Test
    fun openAndCloseMenuFromTopBar() {
        launchScreen()

        clickView(R.id.button_search_menu_back)
        checkViewVisible(R.id.text_update_time)

        clickView(R.id.button_search_menu_back)
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
    fun navigateToGameList() {
        (storage as MockStorage).savedTopLevelScreen = HudFragment.TOP_LEVEL_SCREEN_ID_SONG

        launchScreen()

        checkTopLevelScreen(R.string.app_name, R.string.subtitle_all_sheets)

        checkNavigationButton(
            R.id.layout_by_game,
            R.string.app_name,
            R.string.subtitle_game
        )
    }

    @Test
    fun navigateToJamListThenBackToGameList() {
        launchScreen()

        checkNavigationButton(
            R.id.layout_jams,
            R.string.title_jams,
            null
        )

        checkNavigationButton(
            R.id.layout_by_game,
            R.string.app_name,
            R.string.subtitle_game
        )
    }

    @Test
    fun navigateToSettings() {
        launchScreen()

        checkSearchButtonIsHamburger()

        clickView(R.id.button_menu)
        checkViewVisible(R.id.text_update_time)

        clickView(R.id.layout_settings)
        checkViewNotVisible(R.id.text_update_time)

        checkViewVisible(R.id.checkbox_setting)
        checkSearchButtonIsBackArrow()
    }

    @Test
    fun navigateToDebugSettings() {
        launchScreen()

        checkSearchButtonIsHamburger()

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

        checkSearchButtonIsBackArrow()
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

    private fun checkSearchButtonIsBackArrow() {
        onView(
            withId(R.id.button_search_menu_back)
        ).check(
            matches(
                withContentDescription(R.string.cd_search_back)
            )
        )
    }

    private fun checkSearchButtonIsHamburger() {
        onView(
            withId(R.id.button_search_menu_back)
        ).check(
            matches(
                withContentDescription(R.string.cd_search_menu)
            )
        )
    }
}