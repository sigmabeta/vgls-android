package com.vgleadsheets.remaster.games.detail

import android.content.res.Resources
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.ui.list.ListScreen

@Composable
fun GamesDetailScreen(
    state: State,
    resources: Resources,
    modifier: Modifier
) {
    ListScreen(
        items = state.toListItems(resources),
        modifier = modifier,
    )
}
