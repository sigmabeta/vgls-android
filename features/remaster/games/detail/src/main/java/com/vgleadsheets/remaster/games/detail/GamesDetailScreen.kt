package com.vgleadsheets.remaster.games.detail

import android.content.res.Resources
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vgleadsheets.ui.list.GridScreen

@Composable
fun GamesDetailScreen(
    state: State,
    resources: Resources,
    modifier: Modifier
) {
    GridScreen(
        items = state.toListItems(resources),
        modifier = modifier,
        minSize = 160.dp
    )
}
