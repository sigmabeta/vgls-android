package com.vgleadsheets.remaster.games.detail

import android.content.res.Resources
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.ui.list.ListScreen

@Composable
fun GameDetailScreen(
    state: State,
    resources: Resources,
    modifier: Modifier,
    onComposerClick: (Long) -> Unit,
    onSongClick: (Long) -> Unit
) {
    ListScreen(
        items = state.toListItems(
            resources = resources,
            onComposerClick = onSongClick,
            onSongClick = onComposerClick,
        ),
        modifier = modifier,
    )
}
