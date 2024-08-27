package com.vgleadsheets.composables.previews.screens.lists

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.composables.previews.ScreenPreviewDark
import com.vgleadsheets.composables.previews.ScreenPreviewLight
import com.vgleadsheets.model.generator.FakeModelGenerator
import com.vgleadsheets.model.generator.StringGenerator
import com.vgleadsheets.remaster.composers.list.State
import java.util.Random

@Preview
@Composable
private fun ComposerListLight(modifier: Modifier = Modifier) {
    val screenState = composerScreenState()
    ScreenPreviewLight(screenState, isGrid = true)
}

@Preview
@Composable
private fun ComposerListLoading(modifier: Modifier = Modifier) {
    val screenState = composerScreenLoadingState()
    ScreenPreviewLight(screenState, isGrid = true)
}

@Preview
@Composable
private fun ComposerListDark(modifier: Modifier = Modifier) {
    val screenState = composerScreenState()

    ScreenPreviewDark(screenState, isGrid = true)
}

@Suppress("MagicNumber")
private fun composerScreenState(): State {
    val seed = 1234567L
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
