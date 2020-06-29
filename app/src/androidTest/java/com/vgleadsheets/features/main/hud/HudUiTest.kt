package com.vgleadsheets.features.main.hud

import com.vgleadsheets.MockStorage
import com.vgleadsheets.R
import com.vgleadsheets.UiTest
import org.junit.Test

class HudUiTest : UiTest() {
    override val startingTopLevelScreenTitleId: Int? = null

    @Test
    fun openAndCloseMenuFromTopBar() {
        hud(this) {
            clickView(R.id.button_search_menu_back)
            checkViewVisible(R.id.text_update_time)

            clickView(R.id.button_search_menu_back)
            checkViewNotVisible(R.id.text_update_time)
        }
    }

    @Test
    fun navigateToComposerList() {
        checkNavigationButton(
            R.id.layout_by_composer,
            R.string.app_name,
            R.string.subtitle_composer
        )
    }

    @Test
    fun navigateToTagList() {
        checkNavigationButton(
            R.id.layout_by_tag,
            R.string.app_name,
            R.string.subtitle_tags
        )
    }

    @Test
    fun navigateToSongList() {
        checkNavigationButton(
            R.id.layout_all_sheets,
            R.string.app_name,
            R.string.subtitle_all_sheets
        )
    }

    @Test
    fun navigateToJamList() {
        checkNavigationButton(
            R.id.layout_jams,
            R.string.title_jams,
            null
        )
    }

    @Test
    fun navigateToGameList() {
        (storage as MockStorage).savedTopLevelScreen = HudFragment.TOP_LEVEL_SCREEN_ID_SONG

        hud(this) {
            checkTopLevelScreen(R.string.app_name, R.string.subtitle_all_sheets)
        }

        checkNavigationButton(
            R.id.layout_by_game,
            R.string.app_name,
            R.string.subtitle_game
        )
    }

    @Test
    fun navigateToJamListThenBackToGameList() {
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

        hud(this) {
            checkSearchButtonIsHamburger()

            clickView(R.id.button_menu)
            checkViewVisible(R.id.text_update_time)

            clickView(R.id.layout_settings)
            checkViewNotVisible(R.id.text_update_time)

            checkViewVisible(R.id.checkbox_setting)
            checkSearchButtonIsBackArrow()
        }
    }

    @Test
    fun navigateToDebugSettings() {
        launchScreen()

        hud(this) {
            checkSearchButtonIsHamburger()

            clickView(R.id.button_menu)
            checkViewVisible(R.id.text_update_time)

            clickView(R.id.layout_debug)
            checkViewNotVisible(R.id.text_update_time)

            checkViewWithIdAndTextVisible(
                R.id.text_name,
                R.string.label_debug_network_endpoint
            )

            checkSearchButtonIsBackArrow()
        }
    }

    private fun checkNavigationButton(
        buttonId: Int,
        titleStringId: Int,
        subtitleStringId: Int?
    ) {
        hud(this) {
            clickView(R.id.button_menu)
            checkViewVisible(R.id.text_update_time)

            clickView(buttonId)
            checkViewNotVisible(R.id.text_update_time)

            checkTopLevelScreen(titleStringId, subtitleStringId)
        }
    }
}
