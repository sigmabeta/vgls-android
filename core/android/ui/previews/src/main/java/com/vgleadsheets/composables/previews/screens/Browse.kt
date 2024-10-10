package com.vgleadsheets.composables.previews.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.composables.previews.DevicePreviews
import com.vgleadsheets.composables.previews.ListScreenPreview
import com.vgleadsheets.list.WidthClass
import com.vgleadsheets.remaster.browse.State
import com.vgleadsheets.scaffold.currentWindowWidthClassSynthetic
import com.vgleadsheets.ui.StringProvider
import com.vgleadsheets.ui.StringResources

@DevicePreviews
@Composable
internal fun BrowseScreen(
    darkTheme: Boolean = isSystemInDarkTheme(),
    syntheticWidthClass: WidthClass = currentWindowWidthClassSynthetic(),
) {
    val stringProvider = StringResources(LocalContext.current.resources)
    val screenState = browseScreenState(stringProvider)
    ListScreenPreview(
        screenState = screenState,
        syntheticWidthClass = syntheticWidthClass,
        darkTheme = darkTheme
    )
}

@DevicePreviews
@Composable
internal fun BrowseScreenLoading(
    darkTheme: Boolean = isSystemInDarkTheme(),
    syntheticWidthClass: WidthClass = currentWindowWidthClassSynthetic(),
) {
    val screenState = browseScreenLoadingState()
    ListScreenPreview(
        screenState = screenState,
        syntheticWidthClass = syntheticWidthClass,
        darkTheme = darkTheme
    )
}

@Suppress("MagicNumber")
private fun browseScreenState(stringProvider: StringProvider): State {
    val screenState = State(
        publishDateId = LCE.Content(1726947395652L)
    )
    return screenState
}

@Suppress("MagicNumber")
private fun browseScreenLoadingState(): State {
    val screenState = State(
        publishDateId = LCE.Loading("publishDate")
    )
    return screenState
}
