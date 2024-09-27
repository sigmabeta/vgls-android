package com.vgleadsheets.composables.previews.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.bottombar.NavBarVisibility
import com.vgleadsheets.composables.previews.DevicePreviews
import com.vgleadsheets.composables.previews.ScreenPreview
import com.vgleadsheets.model.Part
import com.vgleadsheets.model.generator.FakeModelGenerator
import com.vgleadsheets.model.generator.StringGenerator
import com.vgleadsheets.topbar.TopBarVisibility
import com.vgleadsheets.ui.viewer.ViewerScreen
import com.vgleadsheets.ui.viewer.ViewerState
import java.util.Random

@DevicePreviews
@Composable
internal fun Viewer(darkTheme: Boolean = isSystemInDarkTheme()) {
    val screenState = viewerScreenState()

    ScreenPreview(
        darkTheme = darkTheme,
        topBarVisibility = TopBarVisibility.HIDDEN,
        navBarVisibility = NavBarVisibility.HIDDEN,
    ) {
        ViewerContent(screenState)
    }
}

@DevicePreviews
@Composable
internal fun ViewerLoading(darkTheme: Boolean = isSystemInDarkTheme()) {
    val screenState = loadingScreenState()

    ScreenPreview(darkTheme = darkTheme) {
        ViewerContent(screenState)
    }
}

@DevicePreviews
@Composable
internal fun ViewerWithChrome(darkTheme: Boolean = isSystemInDarkTheme()) {
    val screenState = viewerScreenState()

    ScreenPreview(darkTheme = darkTheme) {
        ViewerContent(screenState)
    }
}

@DevicePreviews
@Composable
internal fun ViewerLoadingWithChrome(darkTheme: Boolean = isSystemInDarkTheme()) {
    val screenState = loadingScreenState()

    ScreenPreview(darkTheme = darkTheme) {
        ViewerContent(screenState)
    }
}

@Composable
private fun ViewerContent(state: ViewerState) {
    ViewerScreen(
        state = state,
        actionSink = { },
        showDebug = false,
        modifier = Modifier,
    )
}

@Suppress("MagicNumber")
private fun viewerScreenState(): ViewerState {
    val seed = 12345L
    val random = Random(seed)
    val modelGenerator = FakeModelGenerator(
        random,
        seed,
        StringGenerator(random)
    )

    val song = modelGenerator.randomSong()

    val screenState = ViewerState(
        song = song,
        partApiId = Part.E.apiId,
        initialPage = 0,
        buttonsVisible = true,
        isAltSelected = LCE.Content(false),
    )
    return screenState
}

private fun loadingScreenState() = ViewerState(
    song = null,
    partApiId = Part.E.apiId,
    initialPage = 0,
    buttonsVisible = true,
    isAltSelected = LCE.Content(false),
)
