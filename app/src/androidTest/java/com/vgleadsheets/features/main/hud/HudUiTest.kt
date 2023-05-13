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
            checkViewText(R.string.label_settings)

            clickView(R.id.button_search_menu_back)
            checkSearchButtonIsHamburger()
        }
    }

    @Test
    fun navigateToComposerList() {
        checkNavigationButton(
            R.string.label_by_composer,
            R.string.app_name,
            R.string.subtitle_composer
        )
    }

    @Test
    fun navigateToTagList() {
        checkNavigationButton(
            R.string.label_by_tag,
            R.string.app_name,
            R.string.subtitle_tags
        )
    }

    @Test
    fun navigateToSongList() {
        checkNavigationButton(
            R.string.label_all_songs,
            R.string.app_name,
            R.string.subtitle_all_songs
        )
    }

    @Test
    fun navigateToJamList() {
        checkNavigationButton(
            R.string.label_jams,
            R.string.title_jams,
            null
        )
    }

    @Test
    fun navigateToGameList() {
        (storage as MockStorage).savedTopLevelScreen = HudFragment.TOP_LEVEL_SCREEN_ID_SONG

        hud(this) {
            checkTopLevelScreen(R.string.app_name, R.string.subtitle_all_songs)
        }

        checkNavigationButton(
            R.string.label_by_game,
            R.string.app_name,
            R.string.subtitle_game
        )
    }

    @Test
    fun navigateToJamListThenBackToGameList() {
        checkNavigationButton(
            R.string.label_jams,
            R.string.title_jams,
            null
        )

        checkNavigationButton(
            R.string.label_by_game,
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
            checkViewText(R.string.label_settings)

            clickViewWithText(R.string.label_settings)

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

            clickViewWithText(R.string.label_debug)

            checkViewWithIdAndTextVisible(
                R.id.text_name,
                R.string.label_debug_network_endpoint
            )

            checkSearchButtonIsBackArrow()
        }
    }

    private fun checkNavigationButton(
        buttonTextId: Int,
        titleStringId: Int,
        subtitleStringId: Int?
    ) {
        hud(this) {
            clickView(R.id.button_menu)
            checkViewText(R.string.label_settings)

            clickViewWithText(buttonTextId)
            checkSearchButtonIsHamburger()

            checkTopLevelScreen(titleStringId, subtitleStringId)
        }
    }
}
