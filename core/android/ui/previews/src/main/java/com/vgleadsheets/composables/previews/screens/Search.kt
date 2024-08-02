package com.vgleadsheets.composables.previews.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.model.generator.FakeModelGenerator
import com.vgleadsheets.model.generator.StringGenerator
import com.vgleadsheets.search.SearchScreen
import com.vgleadsheets.search.SearchState
import com.vgleadsheets.ui.StringProvider
import com.vgleadsheets.ui.StringResources
import com.vgleadsheets.ui.themes.VglsMaterial
import java.util.Random

@Preview
@Composable
private fun GameListLight(modifier: Modifier = Modifier) {
    val screenState = favoritesScreenState()
    ScreenPreviewLight(screenState, isGrid = true)
}

@Preview
@Composable
private fun GameListDark(modifier: Modifier = Modifier) {
    val screenState = favoritesScreenState()

    ScreenPreviewDark(screenState, isGrid = true)
}

@Suppress("MagicNumber")
private fun favoritesScreenState(): SearchState {
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

    val screenState = SearchState(
        searchQuery = "this is thancred",
        songResults = songs,
        gameResults = games,
        composerResults = composers,
    )
    return screenState
}

@Composable
internal fun ScreenPreviewLight(
    screenState: SearchState,
    isGrid: Boolean = false,
) {
    val actionSink = ActionSink { }
    val stringProvider = StringResources(LocalContext.current.resources)

    VglsMaterial {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            Content(screenState, stringProvider, actionSink)
        }
    }
}

@Composable
internal fun ScreenPreviewDark(
    screenState: SearchState,
    isGrid: Boolean = false,
) {
    val actionSink = ActionSink { }
    val stringProvider = StringResources(LocalContext.current.resources)

    VglsMaterial(forceDark = true) {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            Content(screenState, stringProvider, actionSink)
        }
    }
}

@Composable
private fun Content(
    state: SearchState,
    stringProvider: StringProvider,
    actionSink: ActionSink
) {
    SearchScreen(
        query = state.searchQuery,
        results = state.resultItems(stringProvider),
        actionSink = actionSink,
        modifier = Modifier,
        textFieldUpdater = textFieldUpdater
    )
}
