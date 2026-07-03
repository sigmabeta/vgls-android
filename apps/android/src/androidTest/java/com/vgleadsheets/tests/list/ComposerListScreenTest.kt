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
class ComposerListScreenTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var composeRule = createAndroidComposeRule<RemasteredActivity>()

    @Test
    fun composerListScreen_doesntCrash() {
        composeRule.onNodeWithText("VGLeadSheets").assertExists()
        composeRule.onNodeWithText("Browse").performClick()

        composeRule.onNodeWithText("By Composer").performClick()

        composeRule.onNodeWithText("Alona Franco").assertExists()
    }
}
