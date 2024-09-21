package com.vgleadsheets.composables.previews.screens.details

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.composables.previews.ScreenPreviewDark
import com.vgleadsheets.composables.previews.ScreenPreviewLight
import com.vgleadsheets.model.generator.FakeModelGenerator
import com.vgleadsheets.model.generator.StringGenerator
import com.vgleadsheets.remaster.games.detail.State
import java.util.Random

@Preview
@Composable
internal fun GameDetailLight(modifier: Modifier = Modifier) {
    val screenState = gameScreenState()

    ScreenPreviewLight(screenState)
}

@Preview
@Composable
internal fun GameDetailLoading(modifier: Modifier = Modifier) {
    val screenState = gameScreenLoadingState()

    ScreenPreviewLight(screenState)
}

@Preview
@Composable
internal fun GameDetailDark(modifier: Modifier = Modifier) {
    val screenState = gameScreenState()

    ScreenPreviewDark(screenState)
}

@Preview
@Composable
internal fun GameDetailLoadingDark(modifier: Modifier = Modifier) {
    val screenState = gameScreenLoadingState()

    ScreenPreviewDark(screenState)
}

@Suppress("MagicNumber")
private fun gameScreenState(): State {
    val seed = 1234L
    val random = Random(seed)
    val modelGenerator = FakeModelGenerator(
        random,
        seed,
        StringGenerator(random)
    )

    val game = modelGenerator.randomGame()
    val composers = modelGenerator.randomComposers()
    val songs = modelGenerator.randomSongs()

    val screenState = State(
        game = LCE.Content(game),
        composers = LCE.Content(composers),
        songs = LCE.Content(songs),
        isFavorite = LCE.Content(false),
    )
    return screenState
}

@Suppress("MagicNumber")
private fun gameScreenLoadingState(): State {
    val seed = 1234L
    val random = Random(seed)
    val stringGenerator = StringGenerator(random)

    val screenState = State(
        game = LCE.Loading(stringGenerator.generateName()),
        composers = LCE.Loading(stringGenerator.generateName()),
        songs = LCE.Loading(stringGenerator.generateName()),
        isFavorite = LCE.Loading(stringGenerator.generateName()),
    )
    return screenState
}
