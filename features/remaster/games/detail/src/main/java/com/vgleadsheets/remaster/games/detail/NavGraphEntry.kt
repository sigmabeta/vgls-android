package com.vgleadsheets.remaster.games.detail

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

fun NavGraphBuilder.gamesDetailScreenEntry(navigationAction: (String) -> Unit, globalModifier: Modifier) {
    composable(
        route = "games/{gameId}",
        arguments = listOf(
            navArgument("gameId") { type = NavType.LongType }
        )
    ) {
        val gameId = it.arguments?.getLong("gameId") ?: throw IllegalArgumentException(
            "gameId is required"
        )

        val viewModel = gameDetailViewModel(
            gameId = gameId,
            navigationAction,
        )
        val state by viewModel.uiState.collectAsState()

        GamesDetailScreen(
            state,
            LocalContext.current.resources,
            globalModifier,
            ,
            ,
        )
    }
}
