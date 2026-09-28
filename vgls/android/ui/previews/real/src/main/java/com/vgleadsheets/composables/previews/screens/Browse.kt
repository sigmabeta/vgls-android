package com.vgleadsheets.composables.previews.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.vgleadsheets.composables.previews.DevicePreviews
import com.vgleadsheets.composables.previews.ListScreenPreview
import com.vgleadsheets.remaster.browse.State
import com.vgleadsheets.scaffold.currentWindowWidthClassSynthetic
import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.list.WidthClass
import net.sigmabeta.sage.ui.StringProvider
import com.vgleadsheets.strings.rememberVglsStringProvider

@DevicePreviews
@Composable
internal fun BrowseScreen(
    darkTheme: Boolean = isSystemInDarkTheme(),
    syntheticWidthClass: WidthClass = currentWindowWidthClassSynthetic(),
) {
    val stringProvider = rememberVglsStringProvider()
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
