package com.vgleadsheets.composables.previews.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.composables.previews.DevicePreviews
import com.vgleadsheets.composables.previews.ScreenPreview
import com.vgleadsheets.list.WidthClass
import com.vgleadsheets.model.generator.FakeModelGenerator
import com.vgleadsheets.model.generator.StringGenerator
import com.vgleadsheets.model.history.SearchHistoryEntry
import com.vgleadsheets.scaffold.currentWindowWidthClassSynthetic
import com.vgleadsheets.search.SearchScreen
import com.vgleadsheets.search.SearchState
import com.vgleadsheets.topbar.TopBarVisibility
import com.vgleadsheets.ui.StringProvider
import java.util.Random

@DevicePreviews
@Composable
internal fun SearchScreen(
    darkTheme: Boolean = isSystemInDarkTheme(),
    syntheticWidthClass: WidthClass = currentWindowWidthClassSynthetic(),
) {
    val screenState = searchScreenState()
    ScreenPreview(
        darkTheme = darkTheme,
        syntheticWidthClass = syntheticWidthClass,
        topBarVisibility = TopBarVisibility.HIDDEN
    ) {
        SearchContent(screenState, it)
    }
}

@DevicePreviews
@Composable
internal fun SearchScreenLoading(
    darkTheme: Boolean = isSystemInDarkTheme(),
    syntheticWidthClass: WidthClass = currentWindowWidthClassSynthetic(),
) {
    val screenState = searchScreenLoadingState()
    ScreenPreview(
        darkTheme = darkTheme,
        syntheticWidthClass = syntheticWidthClass,
        topBarVisibility = TopBarVisibility.HIDDEN
    ) {
        SearchContent(screenState, it)
    }
}

@DevicePreviews
@Composable
internal fun SearchScreenHistory(
    darkTheme: Boolean = isSystemInDarkTheme(),
    syntheticWidthClass: WidthClass = currentWindowWidthClassSynthetic(),
) {
    val screenState = searchScreenHistoryState()
    ScreenPreview(
        darkTheme = darkTheme,
        syntheticWidthClass = syntheticWidthClass,
        topBarVisibility = TopBarVisibility.HIDDEN
    ) {
        SearchContent(screenState, it)
    }
}

@DevicePreviews
@Composable
internal fun SearchScreenLoadingHistory(
    darkTheme: Boolean = isSystemInDarkTheme(),
    syntheticWidthClass: WidthClass = currentWindowWidthClassSynthetic(),
) {
    val screenState = searchScreenLoadingHistoryState()

    ScreenPreview(
        darkTheme = darkTheme,
        syntheticWidthClass = syntheticWidthClass,
        topBarVisibility = TopBarVisibility.HIDDEN
    ) {
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
