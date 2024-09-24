package com.vgleadsheets.composables.previews.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.composables.previews.ScreenPreviewDark
import com.vgleadsheets.composables.previews.ScreenPreviewLight
import com.vgleadsheets.model.generator.FakeModelGenerator
import com.vgleadsheets.model.generator.StringGenerator
import com.vgleadsheets.model.history.SearchHistoryEntry
import com.vgleadsheets.search.SearchScreen
import com.vgleadsheets.search.SearchState
import com.vgleadsheets.ui.StringProvider
import java.util.Random

@Preview
@Composable
internal fun SearchScreenLight(modifier: Modifier = Modifier) {
    val screenState = searchScreenState()
    ScreenPreviewLight {
        SearchContent(screenState, it)
    }
}

@Preview
@Composable
internal fun SearchScreenLoading(modifier: Modifier = Modifier) {
    val screenState = searchScreenLoadingState()
    ScreenPreviewLight {
        SearchContent(screenState, it)
    }
}

@Preview
@Composable
internal fun SearchScreenHistory(modifier: Modifier = Modifier) {
    val screenState = searchScreenHistoryState()
    ScreenPreviewLight {
        SearchContent(screenState, it)
    }
}

@Preview
@Composable
internal fun SearchScreenLoadingHistory(modifier: Modifier = Modifier) {
    val screenState = searchScreenLoadingHistoryState()

    ScreenPreviewLight {
        SearchContent(screenState, it)
    }
}

@Preview
@Composable
internal fun SearchScreenDark(modifier: Modifier = Modifier) {
    val screenState = searchScreenState()

    ScreenPreviewDark {
        SearchContent(screenState, it)
    }
}

@Preview
@Composable
internal fun SearchScreenLoadingDark(modifier: Modifier = Modifier) {
    val screenState = searchScreenLoadingState()
    ScreenPreviewDark {
        SearchContent(screenState, it)
    }
}

@Preview
@Composable
internal fun SearchScreenHistoryDark(modifier: Modifier = Modifier) {
    val screenState = searchScreenHistoryState()
    ScreenPreviewDark {
        SearchContent(screenState, it)
    }
}

@Preview
@Composable
internal fun SearchScreenLoadingHistoryDark(modifier: Modifier = Modifier) {
    val screenState = searchScreenLoadingHistoryState()

    ScreenPreviewDark {
        SearchContent(screenState, it)
    }
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
private fun SearchContent(
    state: SearchState,
    stringProvider: StringProvider,
) {
    SearchScreen(
        query = state.searchQuery,
        results = state.toListItems(stringProvider),
        actionSink = { },
        showDebug = true,
        modifier = Modifier,
        textFieldUpdater = { }
    )
}
