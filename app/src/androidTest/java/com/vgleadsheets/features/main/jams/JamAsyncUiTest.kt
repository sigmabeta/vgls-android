package com.vgleadsheets.features.main.jams

import android.annotation.SuppressLint
import com.vgleadsheets.features.main.ListUiTest
import com.vgleadsheets.features.main.hud.HudFragment
import com.vgleadsheets.features.main.viewer.viewer
import org.junit.Test
import java.util.Locale

class JamsAsyncUiTest : ListUiTest() {
    override val screenId = HudFragment.TOP_LEVEL_SCREEN_ID_JAM

    @Test
    fun findJamTestFollowJam() {
        jamList(this) {
            openFindJamDialog()
            typeInSearchBox(NAME_JAM)
            clickFindButton()
            isItemWithTitleDisplayed(NAME_JAM.toTitleCase(), 2)
            clickJamWithTitle(NAME_JAM.toTitleCase())
        }

        jam(this, NAME_JAM.toTitleCase()) {
            clickCtaWithTitle("Follow Jam")
        }

        viewer {
            checkPageVisible(1)
        }
    }

    @Test
    fun findJamTestGoToCurrentSong() {
        jamList(this) {
            openFindJamDialog()
            typeInSearchBox(NAME_JAM)
            clickFindButton()
            isItemWithTitleDisplayed(NAME_JAM.toTitleCase(), 2)
            clickJamWithTitle(NAME_JAM.toTitleCase())
        }

        jam(this, NAME_JAM.toTitleCase()) {
            checkSheetHasTitleAndSubtitle("Pellentesque", "Ultricies Justo", 5)
            clickSheetWithTitle("Pellentesque", 5)
        }

        viewer {
            checkPageVisible(1)
        }
    }

    @Test
    fun findJamTestGoToSetlistEntry() {
        jamList(this) {
            openFindJamDialog()
            typeInSearchBox(NAME_JAM)
            clickFindButton()
            isItemWithTitleDisplayed(NAME_JAM.toTitleCase(), 2)
            clickJamWithTitle(NAME_JAM.toTitleCase())
        }

        jam(this, NAME_JAM.toTitleCase()) {
            checkSheetHasTitleAndSubtitle("In Hendrerit Quisque Est", "Mocks Don't Name Games Yet", 7)
            clickSheetWithTitle("In Hendrerit Quisque Est", 7)
        }

        viewer {
            checkPageVisible(1)
        }
    }

    @Test
    fun findJamTestGoToPastSong() {
        jamList(this) {
            openFindJamDialog()
            typeInSearchBox(NAME_JAM)
            clickFindButton()
            isItemWithTitleDisplayed(NAME_JAM.toTitleCase(), 2)
            clickJamWithTitle(NAME_JAM.toTitleCase())
        }

        jam(this, NAME_JAM.toTitleCase()) {
            checkSheetHasTitleAndSubtitle("Quis", "Velit Eget", 10)
            clickSheetWithTitle("Quis", 10)
        }

        viewer {
            checkPageVisible(1)
        }
    }

    @Test
    fun findJamTestForgetJam() {
        jamList(this) {
            checkIsEmptyStateDisplayed()
            openFindJamDialog()
            typeInSearchBox(NAME_JAM)
            clickFindButton()
            isItemWithTitleDisplayed(NAME_JAM.toTitleCase(), 2)
            clickJamWithTitle(NAME_JAM.toTitleCase())
        }

        jam(this, NAME_JAM.toTitleCase()) {
            clickCtaWithTitle("Forget Jam", 3)
        }

        jamList(this) {
            checkIsEmptyStateDisplayed()
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    @SuppressLint("DefaultLocale")
    private fun String.toTitleCase() = this
        .replace("_", " ")
        .split(" ")
        .map {
            if (it != "the") {
                it.capitalize(Locale.getDefault())
            } else {
                it
            }
        }
        .joinToString(" ")

    companion object {
        const val NAME_JAM = "test"
    }
}