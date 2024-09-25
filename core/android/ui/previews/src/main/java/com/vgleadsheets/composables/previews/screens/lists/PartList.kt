package com.vgleadsheets.composables.previews.screens.lists

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.vgleadsheets.composables.previews.DevicePreviews
import com.vgleadsheets.composables.previews.ListScreenPreview
import com.vgleadsheets.model.Part
import com.vgleadsheets.remaster.parts.State

@DevicePreviews
@Composable
internal fun PartList(darkTheme: Boolean = isSystemInDarkTheme()) {
    val screenState = songScreenState()

    ListScreenPreview(screenState, darkTheme = false)
}

@Suppress("MagicNumber")
private fun songScreenState(): State {
    val screenState = State(
        selectedPart = Part.E,
    )
    return screenState
}
