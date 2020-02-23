package com.vgleadsheets

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.vgleadsheets.features.main.hud.HudFragment
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AppSmokeTest : UiTest() {
    override val startingTopLevelScreenTitleId: Int? = null
    override val startingTopLevelScreenSubtitleId = 0

    @Test
    fun appLaunchesToGamesAndTitleIsVisible() {
        (storage as MockStorage).savedTopLevelScreen = HudFragment.TOP_LEVEL_SCREEN_ID_GAME
        launchScreen()

        checkSuccessfulLaunch(R.string.app_name, R.string.subtitle_game)
    }

    @Test
    fun appLaunchesToComposersAndTitleIsVisible() {
        (storage as MockStorage).savedTopLevelScreen = HudFragment.TOP_LEVEL_SCREEN_ID_COMPOSER
        launchScreen()

        checkSuccessfulLaunch(R.string.app_name, R.string.subtitle_composer)
    }

    @Test
    fun appLaunchesToSongsAndTitleIsVisible() {
        (storage as MockStorage).savedTopLevelScreen = HudFragment.TOP_LEVEL_SCREEN_ID_SONG
        launchScreen()

        checkSuccessfulLaunch(R.string.app_name, R.string.subtitle_all_sheets)
    }

    @Test
    fun appLaunchesToTagsAndTitleIsVisible() {
        (storage as MockStorage).savedTopLevelScreen = HudFragment.TOP_LEVEL_SCREEN_ID_TAG
        launchScreen()

        checkSuccessfulLaunch(R.string.app_name, R.string.subtitle_tags)
    }

    @Test
    fun appLaunchesToJamsAndTitleIsVisible() {
        (storage as MockStorage).savedTopLevelScreen = HudFragment.TOP_LEVEL_SCREEN_ID_JAM
        launchScreen()

        checkSuccessfulLaunch(R.string.title_jams, null)
    }

    private fun checkSuccessfulLaunch(titleStringId: Int, subtitleStringId: Int?) {
        checkViewVisible(R.id.text_title_title)
        checkViewVisible(R.id.text_title_subtitle)

        checkViewText(R.id.text_title_title, titleStringId)

        if (subtitleStringId != null) {
            checkViewText(R.id.text_title_subtitle, subtitleStringId)
        }
    }
}
