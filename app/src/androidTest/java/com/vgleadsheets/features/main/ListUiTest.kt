package com.vgleadsheets.features.main

import com.vgleadsheets.AsyncUiTest
import com.vgleadsheets.MockStorage
import org.junit.Before

abstract class ListUiTest : AsyncUiTest() {
    abstract val screenId: String

    @Before
    override fun setup() {
        super.setup()
        (storage as MockStorage).savedTopLevelScreen = screenId
    }

    override fun launchScreen() {
        super.launchScreen()
        emitDataFromApi()
        waitForUi()
    }
}
