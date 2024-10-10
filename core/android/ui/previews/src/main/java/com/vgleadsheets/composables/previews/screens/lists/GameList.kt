package com.vgleadsheets.composables.previews.screens.lists

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.composables.previews.DevicePreviews
import com.vgleadsheets.composables.previews.ListScreenPreview
import com.vgleadsheets.list.WidthClass
import com.vgleadsheets.model.generator.FakeModelGenerator
import com.vgleadsheets.model.generator.StringGenerator
import com.vgleadsheets.remaster.games.list.State
import com.vgleadsheets.scaffold.currentWindowWidthClassSynthetic
import java.util.Random

@DevicePreviews
@Composable
internal fun GameList(
    darkTheme: Boolean = isSystemInDarkTheme(),
    syntheticWidthClass: WidthClass = currentWindowWidthClassSynthetic(),
) {
    val screenState = gameScreenState()
    ListScreenPreview(
        screenState = screenState,
        syntheticWidthClass = syntheticWidthClass,
        darkTheme = darkTheme
    )
}

@DevicePreviews
@Composable
internal fun GameListLoading(
    darkTheme: Boolean = isSystemInDarkTheme(),
    syntheticWidthClass: WidthClass = currentWindowWidthClassSynthetic(),
) {
    val screenState = gameScreenLoadingState()
    ListScreenPreview(
        screenState = screenState,
        syntheticWidthClass = syntheticWidthClass,
        darkTheme = darkTheme
    )
}

@DevicePreviews
@Composable
internal fun GameListDark(
    darkTheme: Boolean = isSystemInDarkTheme(),
    syntheticWidthClass: WidthClass = currentWindowWidthClassSynthetic(),
) {
    val screenState = gameScreenState()

    ListScreenPreview(
        screenState = screenState,
        syntheticWidthClass = syntheticWidthClass,
        darkTheme = darkTheme
    )
}

@DevicePreviews
@Composable
internal fun GameListDarkLoading(
    darkTheme: Boolean = isSystemInDarkTheme(),
    syntheticWidthClass: WidthClass = currentWindowWidthClassSynthetic(),
) {
    val screenState = gameScreenLoadingState()
    ListScreenPreview(
        screenState = screenState,
        syntheticWidthClass = syntheticWidthClass,
        darkTheme = darkTheme
    )
}

@Suppress("MagicNumber")
private fun gameScreenState(): State {
    val seed = 123456L
    val random = Random(seed)
    val modelGenerator = FakeModelGenerator(
        random,
        seed,
        StringGenerator(random)
    )

    val games = modelGenerator.randomGames()

    val screenState = State(
        games = LCE.Content(games),
    )
    return screenState
}

@Suppress("MagicNumber")
private fun gameScreenLoadingState(): State {
    val seed = 1234567L
    val random = Random(seed)
    val stringGenerator = StringGenerator(random)

    val screenState = State(
        games = LCE.Loading(stringGenerator.generateName()),
    )
    return screenState
}
