package com.vgleadsheets.tests.list

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vgleadsheets.remaster.RemasteredActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class OfflineContentScreenTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var composeRule = createAndroidComposeRule<RemasteredActivity>()

    @Test
    fun offlineContentScreen_doesntCrash() {
        composeRule.onNodeWithText("VGLeadSheets").assertExists()
        composeRule.onNodeWithText("Browse").performClick()

        composeRule.onNodeWithText("Offline-available").performClick()

        composeRule.onNodeWithText(
            "Games, songs, and composers that you\'ve asked to keep available offline. Once you\'ve marked some, they\'ll be here."
        ).assertExists()
    }
}
