package com.vgleadsheets.composables.previews.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.composables.previews.ScreenPreviewDark
import com.vgleadsheets.composables.previews.ScreenPreviewLight
import com.vgleadsheets.model.Part
import com.vgleadsheets.model.generator.FakeModelGenerator
import com.vgleadsheets.model.generator.StringGenerator
import com.vgleadsheets.ui.viewer.ViewerScreen
import com.vgleadsheets.ui.viewer.ViewerState
import java.util.Random

@Preview
@Composable
internal fun ViewerLight(modifier: Modifier = Modifier) {
    val screenState = viewerScreenState()

    ScreenPreviewLight {
        ViewerContent(screenState)
    }
}

@Preview
@Composable
internal fun ViewerLightLoading(modifier: Modifier = Modifier) {
    val screenState = loadingScreenState()

    ScreenPreviewLight {
        ViewerContent(screenState)
    }
}

@Preview
@Composable
internal fun ViewerDark(modifier: Modifier = Modifier) {
    val screenState = viewerScreenState()

    ScreenPreviewDark {
        ViewerContent(screenState)
    }
}

@Preview
@Composable
internal fun ViewerDarkLoading(modifier: Modifier = Modifier) {
    val screenState = loadingScreenState()

    ScreenPreviewDark {
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
