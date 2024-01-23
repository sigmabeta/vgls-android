package com.vgleadsheets.remaster.games.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.ui.list.ListScreen

@Composable
fun GamesDetailScreen(
    state: State,
    modifier: Modifier
) {
    ListScreen(
        items = state.detailItems,
        modifier = modifier,
    )
}
