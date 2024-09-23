package com.vgleadsheets.composables.previews.screens.lists

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vgleadsheets.composables.previews.ScreenPreviewDark
import com.vgleadsheets.composables.previews.ScreenPreviewLight
import com.vgleadsheets.model.Part
import com.vgleadsheets.remaster.parts.State

@Preview
@Composable
internal fun PartListLight(modifier: Modifier = Modifier) {
    val screenState = songScreenState()

    ScreenPreviewLight(screenState)
}

@Preview
@Composable
internal fun PartListDark(modifier: Modifier = Modifier) {
    val screenState = songScreenState()

    ScreenPreviewDark(screenState)
}

@Suppress("MagicNumber")
private fun songScreenState(): State {
    val screenState = State(
        selectedPart = Part.E,
    )
    return screenState
}

