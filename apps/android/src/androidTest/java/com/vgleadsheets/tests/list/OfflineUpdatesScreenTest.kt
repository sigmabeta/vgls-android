package com.vgleadsheets.tests.list

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
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
class OfflineUpdatesScreenTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var composeRule = createAndroidComposeRule<RemasteredActivity>()

    @Test
    fun offlineUpdatesScreen_doesntCrash() {
        composeRule.onNodeWithText("VGLeadSheets").assertExists()
        composeRule.onNodeWithContentDescription("Open Settings Menu").performClick()

        composeRule.onNodeWithText("Offline Update History").performClick()

        composeRule.onNodeWithText("No offline sync runs yet.").assertExists()
    }
}
