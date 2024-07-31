package com.vgleadsheets.composables.previews.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vgleadsheets.composables.previews.ScreenPreviewDark
import com.vgleadsheets.composables.previews.ScreenPreviewLight
import com.vgleadsheets.model.generator.FakeModelGenerator
import com.vgleadsheets.model.generator.StringGenerator
import com.vgleadsheets.remaster.games.list.State
import java.util.Random

@Preview
@Composable
private fun GameListLight(modifier: Modifier = Modifier) {
    val screenState = gameScreenState()
    ScreenPreviewLight(screenState, isGrid = true)
}

@Preview
@Composable
private fun GameListDark(modifier: Modifier = Modifier) {
    val screenState = gameScreenState()

    ScreenPreviewDark(screenState, isGrid = true)
}

@Suppress("MagicNumber")
private fun gameScreenState(): State {
    val seed = 1234567L
    val random = Random(seed)
    val modelGenerator = FakeModelGenerator(
        random,
        seed,
        StringGenerator(random)
    )

    val games = modelGenerator.randomGames()

    val screenState = State(
        games = games,
    )
    return screenState
}
