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

fun NavGraphBuilder.gameDetailScreenEntry(
    navigateTo: (String) -> Unit,
    titleUpdater: (String?) -> Unit,
    globalModifier: Modifier
) {
    composable(
        route = Destination.GAME_DETAIL.idTemplate(),
        arguments = listOf(
            navArgument(ARG_DEST_ID) { type = NavType.LongType }
        )
    ) {
        val resources = LocalContext.current.resources
        val gameId = it.arguments?.getLong(ARG_DEST_ID) ?: throw IllegalArgumentException(
            "$ARG_DEST_ID is required"
        )

        val viewModel = gameDetailViewModel(
            gameId = gameId,
            navigateTo = navigateTo,
        )
        val state by viewModel.uiState.collectAsState()

        GameDetailScreen(
            title = state.title,
            listItems = state.toListItems(
                resources = resources,
                onComposerClick = { clickedId -> navigateTo(Destination.COMPOSER_DETAIL.forId(clickedId)) },
                onSongClick = { clickedId -> navigateTo(Destination.SONG_VIEWER.forId(clickedId)) },
            ),
            titleUpdater = titleUpdater,
            modifier = globalModifier,
        )
    }
}
