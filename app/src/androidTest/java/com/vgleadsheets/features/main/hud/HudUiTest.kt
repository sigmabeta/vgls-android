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
            checkViewText(com.vgleadsheets.features.main.hud.R.string.label_settings)

            clickView(R.id.button_search_menu_back)
            checkSearchButtonIsHamburger()
        }
    }

    @Test
    fun navigateToComposerList() {
        checkNavigationButton(
            com.vgleadsheets.features.main.hud.R.string.label_by_composer,
            com.vgleadsheets.ui_core.R.string.app_name,
            com.vgleadsheets.features.main.composers.R.string.subtitle_composer
        )
    }

    @Test
    fun navigateToTagList() {
        checkNavigationButton(
            com.vgleadsheets.features.main.hud.R.string.label_by_tag,
            com.vgleadsheets.ui_core.R.string.app_name,
            com.vgleadsheets.features.main.tagkeys.R.string.subtitle_tags
        )
    }

    @Test
    fun navigateToSongList() {
        checkNavigationButton(
            com.vgleadsheets.features.main.hud.R.string.label_all_songs,
            com.vgleadsheets.ui_core.R.string.app_name,
            com.vgleadsheets.features.main.songs.R.string.subtitle_all_songs
        )
    }

    @Test
    fun navigateToJamList() {
        checkNavigationButton(
            com.vgleadsheets.features.main.hud.R.string.label_jams,
            R.string.title_jams,
            null
        )
    }

    @Test
    fun navigateToGameList() {
        (storage as MockStorage).savedTopLevelScreen = HudFragment.TOP_LEVEL_SCREEN_ID_SONG

        hud(this) {
            checkTopLevelScreen(
                com.vgleadsheets.ui_core.R.string.app_name,
                com.vgleadsheets.features.main.songs.R.string.subtitle_all_songs
            )
        }

        checkNavigationButton(
            com.vgleadsheets.features.main.hud.R.string.label_by_game,
            com.vgleadsheets.ui_core.R.string.app_name,
            com.vgleadsheets.features.main.games.R.string.subtitle_game
        )
    }

    @Test
    fun navigateToJamListThenBackToGameList() {
        checkNavigationButton(
            com.vgleadsheets.features.main.hud.R.string.label_jams,
            R.string.title_jams,
            null
        )

        checkNavigationButton(
            com.vgleadsheets.features.main.hud.R.string.label_by_game,
            com.vgleadsheets.ui_core.R.string.app_name,
            com.vgleadsheets.features.main.games.R.string.subtitle_game
        )
    }

    @Test
    fun navigateToSettings() {
        launchScreen()

        hud(this) {
            checkSearchButtonIsHamburger()

            clickView(com.vgleadsheets.components.R.id.button_menu)
            checkViewText(com.vgleadsheets.features.main.hud.R.string.label_settings)

            clickViewWithText(com.vgleadsheets.features.main.hud.R.string.label_settings)

            checkViewVisible(com.vgleadsheets.components.R.id.checkbox_setting)
            checkSearchButtonIsBackArrow()
        }
    }

    @Test
    fun navigateToDebugSettings() {
        launchScreen()

        hud(this) {
            checkSearchButtonIsHamburger()

            clickView(com.vgleadsheets.components.R.id.button_menu)

            clickViewWithText(com.vgleadsheets.features.main.hud.R.string.label_debug)

            checkViewWithIdAndTextVisible(
                com.vgleadsheets.components.R.id.text_name,
                com.vgleadsheets.storage.R.string.label_debug_network_endpoint
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
            clickView(com.vgleadsheets.components.R.id.button_menu)
            checkViewText(com.vgleadsheets.features.main.hud.R.string.label_settings)

            clickViewWithText(buttonTextId)
            checkSearchButtonIsHamburger()

            checkTopLevelScreen(titleStringId, subtitleStringId)
        }
    }
}
