package com.vgleadsheets.composables.previews.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.composables.previews.ListScreenPreviewDark
import com.vgleadsheets.composables.previews.ListScreenPreviewLight
import com.vgleadsheets.remaster.browse.State
import com.vgleadsheets.ui.StringProvider
import com.vgleadsheets.ui.StringResources

@Preview
@Composable
internal fun BrowseScreenLight(modifier: Modifier = Modifier) {
    val stringProvider = StringResources(LocalContext.current.resources)
    val screenState = browseScreenState(stringProvider)
    ListScreenPreviewLight(screenState)
}

@Preview
@Composable
internal fun BrowseScreenDark(modifier: Modifier = Modifier) {
    val stringProvider = StringResources(LocalContext.current.resources)
    val screenState = browseScreenState(stringProvider)

    ListScreenPreviewDark(screenState)
}

@Preview
@Composable
internal fun BrowseScreenLightLoading(modifier: Modifier = Modifier) {
    val screenState = browseScreenLoadingState()
    ListScreenPreviewLight(screenState)
}

@Preview
@Composable
internal fun BrowseScreenDarkLoading(modifier: Modifier = Modifier) {
    val screenState = browseScreenLoadingState()

    ListScreenPreviewDark(screenState)
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
