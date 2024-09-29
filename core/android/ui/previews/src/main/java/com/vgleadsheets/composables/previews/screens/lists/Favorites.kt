package com.vgleadsheets.composables.previews.screens.lists

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.composables.previews.DevicePreviews
import com.vgleadsheets.composables.previews.ListScreenPreview
import com.vgleadsheets.list.WidthClass
import com.vgleadsheets.model.generator.FakeModelGenerator
import com.vgleadsheets.model.generator.StringGenerator
import com.vgleadsheets.remaster.favorites.State
import com.vgleadsheets.scaffold.currentWindowWidthClassSynthetic
import java.util.Random

@DevicePreviews
@Composable
internal fun FavoriteList(
    darkTheme: Boolean = isSystemInDarkTheme(),
    syntheticWidthClass: WidthClass = currentWindowWidthClassSynthetic(),
) {
    val screenState = favoritesScreenState()
    ListScreenPreview(
        screenState = screenState,
        syntheticWidthClass = syntheticWidthClass,
        darkTheme = darkTheme
    )
}

@DevicePreviews
@Composable
internal fun FavoriteListLoading(
    darkTheme: Boolean = isSystemInDarkTheme(),
    syntheticWidthClass: WidthClass = currentWindowWidthClassSynthetic(),
) {
    val screenState = favoritesScreenLoadingState()
    ListScreenPreview(
        screenState = screenState,
        syntheticWidthClass = syntheticWidthClass,
        darkTheme = darkTheme
    )
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
