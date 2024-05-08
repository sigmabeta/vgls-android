package com.vgleadsheets.remaster.games.detail

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vgleadsheets.nav.ARG_DEST_ID
import com.vgleadsheets.nav.Destination

fun NavGraphBuilder.gameDetailScreenEntry(navigationAction: (String) -> Unit, globalModifier: Modifier) {
    composable(
        route = Destination.GAME_DETAIL.idTemplate(),
        arguments = listOf(
            navArgument(ARG_DEST_ID) { type = NavType.LongType }
        )
    ) {
        val gameId = it.arguments?.getLong(ARG_DEST_ID) ?: throw IllegalArgumentException(
            "$ARG_DEST_ID is required"
        )

        val viewModel = gameDetailViewModel(
            gameId = gameId,
            navigationAction,
        )
        val state by viewModel.uiState.collectAsState()

        GameDetailScreen(
            state.toListItems(
                resources = LocalContext.current.resources,
                onComposerClick = { clickedId -> navigationAction(Destination.COMPOSER_DETAIL.forId(clickedId)) },
                onSongClick = { clickedId -> navigationAction(Destination.SONG_VIEWER.forId(clickedId)) },
            ),
            Modifier,
        )
    }
}
