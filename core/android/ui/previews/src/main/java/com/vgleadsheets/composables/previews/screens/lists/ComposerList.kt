package com.vgleadsheets.composables.previews.screens.lists

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.composables.previews.DevicePreviews
import com.vgleadsheets.composables.previews.ListScreenPreview
import com.vgleadsheets.model.generator.FakeModelGenerator
import com.vgleadsheets.model.generator.StringGenerator
import com.vgleadsheets.remaster.composers.list.State
import java.util.Random

@DevicePreviews
@Composable
internal fun ComposerList(darkTheme: Boolean = isSystemInDarkTheme()) {
    val screenState = composerScreenState()
    ListScreenPreview(screenState, isGrid = true, darkTheme = darkTheme)
}

@DevicePreviews
@Composable
internal fun ComposerListLoading(darkTheme: Boolean = isSystemInDarkTheme()) {
    val screenState = composerScreenLoadingState()
    ListScreenPreview(screenState, isGrid = true, darkTheme = darkTheme)
}

@Suppress("MagicNumber")
private fun composerScreenState(): State {
    val seed = 123456L
    val random = Random(seed)
    val modelGenerator = FakeModelGenerator(
        random,
        seed,
        StringGenerator(random)
    )

    val composers = modelGenerator.randomComposers()

    val screenState = State(
        composers = LCE.Content(composers),
    )
    return screenState
}

@Suppress("MagicNumber")
private fun composerScreenLoadingState(): State {
    val seed = 1234567L
    val random = Random(seed)
    val stringGenerator = StringGenerator(random)

    val screenState = State(
        composers = LCE.Loading(stringGenerator.generateName()),
    )
    return screenState
}
