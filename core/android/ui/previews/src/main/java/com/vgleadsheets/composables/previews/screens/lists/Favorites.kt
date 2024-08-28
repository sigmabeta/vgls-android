package com.vgleadsheets.composables.previews.screens.lists

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.composables.previews.ScreenPreviewDark
import com.vgleadsheets.composables.previews.ScreenPreviewLight
import com.vgleadsheets.model.generator.FakeModelGenerator
import com.vgleadsheets.model.generator.StringGenerator
import com.vgleadsheets.remaster.favorites.State
import java.util.Random

@Preview
@Composable
private fun FavoriteListLight(modifier: Modifier = Modifier) {
    val screenState = favoritesScreenState()
    ScreenPreviewLight(screenState, isGrid = true)
}

@Preview
@Composable
private fun FavoriteListLoading(modifier: Modifier = Modifier) {
    val screenState = favoritesScreenLoadingState()
    ScreenPreviewLight(screenState, isGrid = true)
}

@Preview
@Composable
private fun FavoriteListDark(modifier: Modifier = Modifier) {
    val screenState = favoritesScreenState()

    ScreenPreviewDark(screenState, isGrid = true)
}

@Suppress("MagicNumber")
private fun favoritesScreenState(): State {
    val seed = 1234L
    val random = Random(seed)
    val modelGenerator = FakeModelGenerator(
        random,
        seed,
        StringGenerator(random)
    )

    val songs = modelGenerator.randomSongs()
    val games = modelGenerator.randomGames()
    val composers = modelGenerator.randomComposers()

    val screenState = State(
        favoriteSongs = LCE.Content(songs),
        favoriteGames = LCE.Content(games),
        favoriteComposers = LCE.Content(composers),
    )
    return screenState
}

@Suppress("MagicNumber")
private fun favoritesScreenLoadingState(): State {
    val seed = 1234L
    val random = Random(seed)
    val stringGenerator = StringGenerator(random)

    val screenState = State(
        favoriteSongs = LCE.Loading(stringGenerator.generateName()),
        favoriteGames = LCE.Loading(stringGenerator.generateName()),
        favoriteComposers = LCE.Loading(stringGenerator.generateName()),
    )
    return screenState
}
