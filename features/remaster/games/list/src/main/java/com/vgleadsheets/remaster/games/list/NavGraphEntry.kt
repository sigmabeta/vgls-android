package com.vgleadsheets.remaster.games.list

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.gamesListScreenEntry(navigationAction: (String) -> Unit, globalModifier: Modifier) {
    composable("games") {
        val viewModel = gameListViewModel(navigationAction)
        val state by viewModel.uiState.collectAsState()

        GamesListScreen(
            state.toListItems(
                resources = LocalContext.current.resources,
                onGameClick = { navigationAction("games/$it") }
            ),
            globalModifier
        )
    }
}
