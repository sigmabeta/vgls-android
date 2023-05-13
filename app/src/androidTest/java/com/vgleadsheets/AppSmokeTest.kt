package com.vgleadsheets

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.vgleadsheets.features.main.hud.HudFragment
import com.vgleadsheets.features.main.hud.hud
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AppSmokeTest : UiTest() {
    override val startingTopLevelScreenTitleId: Int? = null

    @Test
    fun appLaunchesToGamesAndTitleIsVisible() {
        (storage as MockStorage).savedTopLevelScreen = HudFragment.TOP_LEVEL_SCREEN_ID_GAME
        launchScreen()

        checkSuccessfulLaunch(
            com.vgleadsheets.ui_core.R.string.app_name,
            com.vgleadsheets.features.main.games.R.string.subtitle_game
        )
    }

    @Test
    fun appLaunchesToComposersAndTitleIsVisible() {
        (storage as MockStorage).savedTopLevelScreen = HudFragment.TOP_LEVEL_SCREEN_ID_COMPOSER
        launchScreen()

        checkSuccessfulLaunch(
            com.vgleadsheets.ui_core.R.string.app_name,
            com.vgleadsheets.features.main.composers.R.string.subtitle_composer
        )
    }

    @Test
    fun appLaunchesToSongsAndTitleIsVisible() {
        (storage as MockStorage).savedTopLevelScreen = HudFragment.TOP_LEVEL_SCREEN_ID_SONG
        launchScreen()

        checkSuccessfulLaunch(
            com.vgleadsheets.ui_core.R.string.app_name,
            com.vgleadsheets.features.main.songs.R.string.subtitle_all_songs
        )
    }

    @Test
    fun appLaunchesToTagsAndTitleIsVisible() {
        (storage as MockStorage).savedTopLevelScreen = HudFragment.TOP_LEVEL_SCREEN_ID_TAG
        launchScreen()

        checkSuccessfulLaunch(
            com.vgleadsheets.ui_core.R.string.app_name,
            com.vgleadsheets.features.main.tagkeys.R.string.subtitle_tags
        )
    }

    @Test
    fun appLaunchesToJamsAndTitleIsVisible() {
        (storage as MockStorage).savedTopLevelScreen = HudFragment.TOP_LEVEL_SCREEN_ID_JAM
        launchScreen()

        checkSuccessfulLaunch(R.string.title_jams, null)
    }

    private fun checkSuccessfulLaunch(titleStringId: Int, subtitleStringId: Int?) {
        hud(this) {
            checkViewVisible(com.vgleadsheets.features.main.list.R.id.text_title_title)
            checkViewVisible(com.vgleadsheets.features.main.list.R.id.text_title_subtitle)

            checkViewText(com.vgleadsheets.features.main.list.R.id.text_title_title, titleStringId)

            if (subtitleStringId != null) {
                checkViewText(
                    com.vgleadsheets.features.main.list.R.id.text_title_subtitle,
                    subtitleStringId
                )
            }
        }
    }
}
