package com.vgleadsheets.features.main

import android.view.View
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isNotChecked
import androidx.test.espresso.matcher.ViewMatchers.withChild
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.vgleadsheets.RecyclerViewMatcher
import com.vgleadsheets.Robot
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher

abstract class ListRobot(test: ListUiTest) : Robot(test) {
    protected val resources by lazy { test.activityRule.activity.resources }

    override fun checkScreenHeader(title: String, subtitle: String) {
        scrollHelper(0) {
            super.checkScreenHeader(title, subtitle)
        }
    }

    fun isHeaderWithTitleDisplayed(text: String, scrollPosition: Int? = null) {
        scrollHelper(scrollPosition) {
            isHeaderWithTitleDisplayedHelper(text)
        }
    }

    fun isItemWithTitleDisplayed(text: String, scrollPosition: Int? = null) {
        scrollHelper(scrollPosition) {
            isItemWithTitleDisplayedHelper(text)
        }
    }

    fun checkSheetHasTitleAndSubtitle(
        title: String,
        subtitle: String,
        scrollPosition: Int? = null
    ) {
        scrollHelper(scrollPosition) {
            onView(
                allOf(
                    withId(com.vgleadsheets.components.R.id.component_image_name_caption),
                    withChild(
                        allOf(
                            withId(com.vgleadsheets.components.R.id.text_name),
                            withText(title)
                        )
                    ),
                    withChild(
                        allOf(
                            withId(com.vgleadsheets.components.R.id.text_caption),
                            withText(subtitle)
                        )
                    )
                )
            ).check(
                matches(
                    isDisplayed()
                )
            )
        }
    }

    fun clickSheetWithTitle(title: String, scrollPosition: Int? = null) {
        scrollHelper(scrollPosition) {
            clickComponentWithTitle(
                com.vgleadsheets.components.R.id.component_image_name_caption,
                title
            )
        }
    }

    fun clickJamWithTitle(title: String, scrollPosition: Int? = null) {
        scrollHelper(scrollPosition) {
            clickComponentWithTitle(com.vgleadsheets.components.R.id.component_name_caption, title)
        }
    }

    fun clickCtaWithTitle(title: String, scrollPosition: Int? = null) {
        scrollHelper(scrollPosition) {
            clickComponentWithTitle(com.vgleadsheets.components.R.id.component_cta, title)
        }
    }

    fun clickLabelValueWithLabel(title: String, scrollPosition: Int? = null) {
        scrollHelper(scrollPosition) {
            clickComponentWithTitle(com.vgleadsheets.components.R.id.component_label_value, title)
        }
    }

    fun clickRatingWithLabel(title: String, scrollPosition: Int? = null) {
        scrollHelper(scrollPosition) {
            clickComponentWithTitle(com.vgleadsheets.components.R.id.component_label_rating, title)
        }
    }

    fun clickComposerWithTitle(title: String, scrollPosition: Int? = null) {
        scrollHelper(scrollPosition) {
            clickComponentWithTitle(
                com.vgleadsheets.components.R.id.component_image_name_caption,
                title
            )
        }
    }

    fun clickGameWithTitle(title: String, scrollPosition: Int? = null) {
        scrollHelper(scrollPosition) {
            clickComponentWithTitle(
                com.vgleadsheets.components.R.id.component_image_name_caption,
                title
            )
        }
    }

    fun clickTagWithTitle(title: String, scrollPosition: Int? = null) {
        scrollHelper(scrollPosition) {
            clickComponentWithTitle(com.vgleadsheets.components.R.id.component_name_caption, title)
        }
    }

    fun clickCheckboxWithTitle(title: String, scrollPosition: Int? = null) {
        scrollHelper(scrollPosition) {
            clickComponentWithTitle(com.vgleadsheets.components.R.id.component_checkable, title)
        }
    }

    fun clickLinkWithTitle(title: String, scrollPosition: Int? = null) {
        scrollHelper(scrollPosition) {
            clickComponentWithTitle(com.vgleadsheets.components.R.id.component_single_line, title)
        }
    }

    fun clickTwoLineLinkWithTitle(title: String, scrollPosition: Int? = null) {
        scrollHelper(scrollPosition) {
            clickComponentWithTitle(com.vgleadsheets.components.R.id.component_name_caption, title)
        }
    }

    protected fun checkIsEmptyStateDisplayedInternal(emptyStateLabel: String) {
        checkFirstContentItem(
            hasDescendant(
                withText("No $emptyStateLabel found at all. Check your internet connection?")
            )
        )
    }

    protected fun checkFirstItemHasTitleInternal(title: String) {
        checkFirstContentItem(
            hasDescendant(
                withText(title)
            )
        )
    }

    protected fun checkFirstItemHasSubtitleInternal(subtitle: String) {
        checkFirstContentItem(
            hasDescendant(
                withText(subtitle)
            )
        )
    }

    protected fun checkBooleanSettingValueIsInternal(
        title: String,
        value: Boolean,
        scrollPosition: Int? = null
    ) {
        scrollHelper(scrollPosition) {
            val checkBox = onView(
                allOf(
                    withParent(
                        withChild(
                            allOf(
                                withId(
                                    com.vgleadsheets.components.R.id.text_name
                                ),
                                withText(
                                    title
                                )
                            )
                        )
                    ),
                    withId(
                        com.vgleadsheets.components.R.id.checkbox_setting
                    )
                )
            )

            if (value) {
                checkBox.check(
                    matches(
                        isChecked()
                    )
                )
            } else {
                checkBox.check(
                    matches(
                        isNotChecked()
                    )
                )
            }
        }
    }

    private fun clickComponentWithTitle(@IdRes componentType: Int, title: String) {
        onView(
            allOf(
                withId(componentType),
                withChild(
                    allOf(
                        withId(com.vgleadsheets.components.R.id.text_name),
                        withText(title)
                    )
                )
            )
        ).perform(
            click()
        )
    }

    private fun clickComponentWithContentDescription(
        @IdRes componentType: Int,
        description: String
    ) {
        onView(
            allOf(
                withId(componentType),
                withChild(
                    allOf(
                        withContentDescription(description)
                    )
                )
            )
        ).perform(
            click()
        )
    }

    private fun checkFirstContentItem(
        matcher: Matcher<View>?
    ) {
        val contentView = RecyclerViewMatcher()

        onView(
            contentView.atPosition(1)
        ).check(
            matches(
                matcher
            )
        )
    }

    private fun isHeaderWithTitleDisplayedHelper(text: String) {
        onView(
            allOf(
                withId(
                    com.vgleadsheets.components.R.id.text_header_name
                ),
                withText(
                    text
                )
            )
        ).check(
            matches(
                isDisplayed()
            )
        )
    }

    private fun isItemWithTitleDisplayedHelper(text: String) {
        onView(
            allOf(
                withId(
                    com.vgleadsheets.components.R.id.text_name
                ),
                withText(
                    text
                )
            )
        ).check(
            matches(
                isDisplayed()
            )
        )
    }

    private fun scrollHelper(scrollPosition: Int?, afterScroll: () -> Unit) {
        if (scrollPosition != null) {
            scrollTo(0)
            scrollTo(scrollPosition)
        }

        afterScroll()
    }

    private fun scrollTo(scrollPosition: Int) {
        onView(
            withId(
                com.vgleadsheets.features.main.about.R.id.list_content
            )
        ).perform(
            RecyclerViewActions.scrollToPosition<ComponentViewHolder>(scrollPosition)
        )
    }
}
