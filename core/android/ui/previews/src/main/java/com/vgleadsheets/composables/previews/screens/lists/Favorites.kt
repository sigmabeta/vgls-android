package com.vgleadsheets.composables.previews.screens.lists

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.composables.previews.ListScreenPreviewDark
import com.vgleadsheets.composables.previews.ListScreenPreviewLight
import com.vgleadsheets.model.generator.FakeModelGenerator
import com.vgleadsheets.model.generator.StringGenerator
import com.vgleadsheets.remaster.favorites.State
import java.util.Random

@Preview
@Composable
internal fun FavoriteListLight(modifier: Modifier = Modifier) {
    val screenState = favoritesScreenState()
    ListScreenPreviewLight(screenState, isGrid = true)
}

@Preview
@Composable
internal fun FavoriteListLightLoading(modifier: Modifier = Modifier) {
    val screenState = favoritesScreenLoadingState()
    ListScreenPreviewLight(screenState, isGrid = true)
}

@Preview
@Composable
internal fun FavoriteListDark(modifier: Modifier = Modifier) {
    val screenState = favoritesScreenState()

    ListScreenPreviewDark(screenState, isGrid = true)
}

@Preview
@Composable
internal fun FavoriteListDarkLoading(modifier: Modifier = Modifier) {
    val screenState = favoritesScreenLoadingState()
    ListScreenPreviewDark(screenState, isGrid = true)
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

    val songs = modelGenerator.randomSongs().take(3)
    val games = modelGenerator.randomGames().take(4)
    val composers = modelGenerator.randomComposers().take(4)

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
