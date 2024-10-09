package com.vgleadsheets.composables.previews.screens.lists

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.jakewharton.threetenabp.AndroidThreeTen
import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.composables.previews.DevicePreviews
import com.vgleadsheets.composables.previews.ListScreenPreview
import com.vgleadsheets.list.WidthClass
import com.vgleadsheets.model.updates.AppUpdate
import com.vgleadsheets.remaster.updates.State
import com.vgleadsheets.scaffold.currentWindowWidthClassSynthetic
import com.vgleadsheets.ui.StringProvider
import com.vgleadsheets.ui.StringResources

@DevicePreviews
@Composable
internal fun UpdateScreen(
    darkTheme: Boolean = isSystemInDarkTheme(),
    syntheticWidthClass: WidthClass = currentWindowWidthClassSynthetic(),
) {
    AndroidThreeTen.init(LocalContext.current)
    val stringProvider = StringResources(LocalContext.current.resources)
    val screenState = updateScreenState(stringProvider)
    ListScreenPreview(
        screenState = screenState,
        syntheticWidthClass = syntheticWidthClass,
        darkTheme = darkTheme
    )
}

@DevicePreviews
@Composable
internal fun UpdateScreenLoading(
    darkTheme: Boolean = isSystemInDarkTheme(),
    syntheticWidthClass: WidthClass = currentWindowWidthClassSynthetic(),
) {
    val screenState = updateScreenLoadingState()
    ListScreenPreview(
        screenState = screenState,
        syntheticWidthClass = syntheticWidthClass,
        darkTheme = darkTheme
    )
}

@Suppress("MagicNumber", "MaxLineLength")
private fun updateScreenState(stringProvider: StringProvider): State {
    val screenState = State(
        updates = LCE.Content(
            listOf(
                AppUpdate(
                    versionCode = 123456,
                    versionName = "3.4.5",
                    releaseDate = System.currentTimeMillis(),
                    changes = listOf(
                        "Here's something that was changed in this version.",
                        "We fixed another thing, which hopefully you'll enjoy.",
                        "I'm going to say a bunch of sentences here. So it's longer. Long text needs to be tested, you see. Kurikaesu: I'm going to say a bunch of sentences here. So it's longer. Long text needs to be tested, you see.",
                    )
                ),
                AppUpdate(
                    versionCode = 12345,
                    versionName = "2.3.4",
                    releaseDate = System.currentTimeMillis() - 100000000L,
                    changes = listOf(
                        "This is a different update.",
                        "I'm going to say a bunch of sentences here. So it's longer. Long text needs to be tested, you see. Kurikaesu: I'm going to say a bunch of sentences here. So it's longer. Long text needs to be tested, you see.",
                        "Here's something that was changed in this version.",
                    )
                ),
            )
        )
    )
    return screenState
}

@Suppress("MagicNumber")
private fun updateScreenLoadingState(): State {
    val screenState = State(
        updates = LCE.Loading("updates")
    )
    return screenState
}
