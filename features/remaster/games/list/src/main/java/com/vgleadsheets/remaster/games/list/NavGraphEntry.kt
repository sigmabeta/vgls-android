package com.vgleadsheets.remaster.games.list

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.vgleadsheets.games.list.R
import com.vgleadsheets.nav.Destination

fun NavGraphBuilder.gamesListScreenEntry(
    navigateTo: (String) -> Unit,
    titleUpdater: (String?) -> Unit,
    globalModifier: Modifier
) {
    composable(Destination.GAMES_LIST.noArgs()) {
        val resources = LocalContext.current.resources
        val viewModel = gameListViewModel(
            navigateTo,
        )
        val state by viewModel.uiState.collectAsState()

        GamesListScreen(
            title = resources.getString(R.string.title_list_games),
            listItems = state.toListItems(
                resources = LocalContext.current.resources,
                onGameClick = { clickedId -> navigateTo(Destination.GAME_DETAIL.forId(clickedId)) }
            ),
            titleUpdater = titleUpdater,
            modifier = globalModifier
        )
    }
}
