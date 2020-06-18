package com.vgleadsheets

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.vgleadsheets.database.VglsDatabase
import com.vgleadsheets.main.MainActivity
import com.vgleadsheets.network.MockVglsApi
import com.vgleadsheets.network.VglsApi
import com.vgleadsheets.storage.Storage
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

abstract class UiTest {
    @Inject
    lateinit var storage: Storage

    @Inject
    lateinit var vglsApi: VglsApi

    @Inject
    lateinit var vglsDatabase: VglsDatabase

    @get:Rule
    open val activityRule = ActivityTestRule(
        MainActivity::class.java,
        false,
        false
    )

    private var screenLaunched: Boolean = false

    @Before
    open fun setup() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val app = instrumentation.targetContext.applicationContext as UiTestApplication
        val component = app.testComponent

        component.inject(this)

        val api = vglsApi as MockVglsApi
        api.maxSongs = 50
    }

    open val startingTopLevelScreenTitleId: Int? = R.string.app_name

    open fun launchScreen() {
        if (screenLaunched) return

        activityRule.launchActivity(null)

        screenLaunched = true
    }

    protected fun setApiToReturnBlank() {
        (vglsApi as MockVglsApi).generateEmptyState = true
    }
}
