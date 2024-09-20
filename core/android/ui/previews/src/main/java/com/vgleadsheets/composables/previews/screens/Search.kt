package com.vgleadsheets.composables.previews.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.model.generator.FakeModelGenerator
import com.vgleadsheets.model.generator.StringGenerator
import com.vgleadsheets.model.history.SearchHistoryEntry
import com.vgleadsheets.search.SearchScreen
import com.vgleadsheets.search.SearchState
import com.vgleadsheets.ui.StringProvider
import com.vgleadsheets.ui.StringResources
import com.vgleadsheets.ui.themes.VglsMaterial
import java.util.Random

@Preview
@Composable
internal fun SearchScreenLight(modifier: Modifier = Modifier) {
    val screenState = searchScreenState()
    ScreenPreviewLight(screenState, isGrid = true)
}

@Preview
@Composable
internal fun SearchScreenLoading(modifier: Modifier = Modifier) {
    val screenState = searchScreenLoadingState()
    ScreenPreviewLight(screenState, isGrid = true)
}

@Preview
@Composable
internal fun SearchScreenHistory(modifier: Modifier = Modifier) {
    val screenState = searchScreenHistoryState()
    ScreenPreviewLight(screenState, isGrid = true)
}

@Preview
@Composable
internal fun SearchScreenLoadingHistory(modifier: Modifier = Modifier) {
    val screenState = searchScreenLoadingHistoryState()

    ScreenPreviewLight(screenState, isGrid = true)
}

@Preview
@Composable
internal fun SearchScreenDark(modifier: Modifier = Modifier) {
    val screenState = searchScreenState()

    ScreenPreviewDark(screenState, isGrid = true)
}

@Preview
@Composable
internal fun SearchScreenLoadingDark(modifier: Modifier = Modifier) {
    val screenState = searchScreenLoadingState()
    com.vgleadsheets.composables.previews.ScreenPreviewDark(screenState, isGrid = true)
}

@Preview
@Composable
internal fun SearchScreenHistoryDark(modifier: Modifier = Modifier) {
    val screenState = searchScreenHistoryState()
    ScreenPreviewDark(screenState, isGrid = true)
}

@Preview
@Composable
internal fun SearchScreenLoadingHistoryDark(modifier: Modifier = Modifier) {
    val screenState = searchScreenLoadingHistoryState()

    ScreenPreviewDark(screenState, isGrid = true)
}

@Suppress("MagicNumber")
private fun searchScreenState(): SearchState {
    val seed = 1234L
    val random = Random(seed)
    val modelGenerator = FakeModelGenerator(
        random,
        seed,
        StringGenerator(random)
    )

    val songs = modelGenerator.randomSongs().take(4)
    val games = modelGenerator.randomGames().take(2)
    val composers = modelGenerator.randomComposers().take(4)

    val screenState = SearchState(
        searchQuery = "this is thancred",
        songResults = LCE.Content(songs),
        gameResults = LCE.Content(games),
        composerResults = LCE.Content(composers),
    )
    return screenState
}

@Suppress("MagicNumber")
private fun searchScreenLoadingState(): SearchState {
    val seed = 12346L
    val random = Random(seed)
    val stringGenerator = StringGenerator(random)

    val results = List(random.nextInt(20)) {
        SearchHistoryEntry(
            id = random.nextLong(),
            query = stringGenerator.generateTitle(),
            timeMs = random.nextLong(),
        )
    }

    val screenState = SearchState(
        searchQuery = stringGenerator.generateName() + " - " + stringGenerator.generateTitle(),
        searchHistory = LCE.Content(results),
        songResults = LCE.Loading(stringGenerator.generateName()),
        gameResults = LCE.Loading(stringGenerator.generateName()),
        composerResults = LCE.Loading(stringGenerator.generateName()),
    )
    return screenState
}

@Suppress("MagicNumber")
private fun searchScreenHistoryState(): SearchState {
    val seed = 12345L
    val random = Random(seed)
    val stringGenerator = StringGenerator(random)

    val results = List(random.nextInt(20)) {
        SearchHistoryEntry(
            id = random.nextLong(),
            query = stringGenerator.generateTitle(),
            timeMs = random.nextLong(),
        )
    }

    val screenState = SearchState(
        searchQuery = "",
        searchHistory = LCE.Content(results),
        songResults = LCE.Uninitialized,
        gameResults = LCE.Uninitialized,
        composerResults = LCE.Uninitialized,
    )
    return screenState
}

@Suppress("MagicNumber")
private fun searchScreenLoadingHistoryState(): SearchState {
    val seed = 12345L
    val random = Random(seed)
    val stringGenerator = StringGenerator(random)

    val screenState = SearchState(
        searchQuery = "",
        searchHistory = LCE.Loading(stringGenerator.generateName()),
        songResults = LCE.Uninitialized,
        gameResults = LCE.Uninitialized,
        composerResults = LCE.Uninitialized,
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
        results = state.toListItems(stringProvider),
        actionSink = actionSink,
        showDebug = true,
        modifier = Modifier,
        textFieldUpdater = { }
    )
}
