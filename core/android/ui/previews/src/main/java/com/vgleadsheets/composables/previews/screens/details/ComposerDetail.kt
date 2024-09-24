package com.vgleadsheets.composables.previews.screens.details

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.composables.previews.ListScreenPreviewDark
import com.vgleadsheets.composables.previews.ListScreenPreviewLight
import com.vgleadsheets.model.generator.FakeModelGenerator
import com.vgleadsheets.model.generator.StringGenerator
import com.vgleadsheets.remaster.composers.detail.State
import java.util.Random

@Preview
@Composable
internal fun ComposerDetailLight(modifier: Modifier = Modifier) {
    val screenState = composerScreenState()

    ListScreenPreviewLight(screenState)
}

@Preview
@Composable
internal fun ComposerDetailLoading(modifier: Modifier = Modifier) {
    val screenState = composerScreenLoadingState()

    ListScreenPreviewLight(screenState)
}

@Preview
@Composable
internal fun ComposerDetailDark(modifier: Modifier = Modifier) {
    val screenState = composerScreenState()

    ListScreenPreviewDark(screenState)
}

@Preview
@Composable
internal fun ComposerDetailLoadingDark(modifier: Modifier = Modifier) {
    val screenState = composerScreenLoadingState()

    ListScreenPreviewLight(screenState)
}

@Suppress("MagicNumber")
private fun composerScreenState(): State {
    val seed = 1234L
    val random = Random(seed)
    val modelGenerator = FakeModelGenerator(
        random,
        seed,
        StringGenerator(random)
    )

    val composer = modelGenerator.randomComposer()
    val games = modelGenerator.randomGames()
    val songs = modelGenerator.randomSongs()

    val screenState = State(
        composer = LCE.Content(composer),
        games = LCE.Content(games),
        songs = LCE.Content(songs),
        isFavorite = LCE.Content(false),
    )
    return screenState
}

@Suppress("MagicNumber")
private fun composerScreenLoadingState(): State {
    val seed = 1234L
    val random = Random(seed)
    val stringGenerator = StringGenerator(random)

    val screenState = State(
        composer = LCE.Loading(stringGenerator.generateName()),
        games = LCE.Loading(stringGenerator.generateName()),
        songs = LCE.Loading(stringGenerator.generateName()),
        isFavorite = LCE.Loading(stringGenerator.generateName()),
    )
    return screenState
}
