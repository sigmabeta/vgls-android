package com.vgleadsheets

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.vgleadsheets.main.MainActivity
import com.vgleadsheets.network.MockVglsApi
import com.vgleadsheets.network.VglsApi
import com.vgleadsheets.storage.Storage
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

abstract class UiTest {
    @Inject
    lateinit var storage: Storage

    @Inject
    lateinit var vglsApi: VglsApi

    @get:Rule
    val activityRule = ActivityTestRule(
        MainActivity::class.java,
        false,
        false
    )

    @Before
    open fun setup() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val app = instrumentation.targetContext.applicationContext as UiTestApplication
        val component = app.testComponent

        component.inject(this)

        val api = vglsApi as MockVglsApi
        api.maxSongs = 50
    }

    protected fun launchScreen() {
        activityRule.launchActivity(null)
    }

    protected fun checkViewText(viewId: Int, textId: Int) {
        onView(
            withId(viewId)
        ).check(
            matches(
                withText(textId)
            )
        )
    }

    protected fun checkViewVisible(viewId: Int) {
        onView(
            withId(viewId)
        ).check(
            matches(
                isDisplayed()
            )
        )
    }

    protected fun checkViewNotVisible(viewId: Int) {
        onView(
            withId(viewId)
        ).check(
            matches(
                not(
                    isDisplayed()
                )
            )
        )
    }

    protected fun checkViewText(viewId: Int, text: String) {
        onView(
            withId(viewId)
        ).check(
            matches(
                withText(text)
            )
        )
    }

    protected fun clickView(viewId: Int) {
        onView(
            withId(viewId)
        ).perform(
            click()
        )
    }
}
