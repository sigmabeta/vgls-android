package com.vgleadsheets.composables.previews.screens.lists

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vgleadsheets.composables.previews.ListScreenPreviewDark
import com.vgleadsheets.composables.previews.ListScreenPreviewLight
import com.vgleadsheets.model.Part
import com.vgleadsheets.remaster.parts.State

@Preview
@Composable
internal fun PartListLight(modifier: Modifier = Modifier) {
    val screenState = songScreenState()

    ListScreenPreviewLight(screenState)
}

@Preview
@Composable
internal fun PartListDark(modifier: Modifier = Modifier) {
    val screenState = songScreenState()

    ListScreenPreviewDark(screenState)
}

@Suppress("MagicNumber")
private fun songScreenState(): State {
    val screenState = State(
        selectedPart = Part.E,
    )
    return screenState
}
