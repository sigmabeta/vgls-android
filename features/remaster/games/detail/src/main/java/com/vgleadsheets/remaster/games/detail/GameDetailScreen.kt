package com.vgleadsheets.remaster.games.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.ui.list.ListScreen
import kotlinx.collections.immutable.ImmutableList

@Composable
fun GameDetailScreen(
    listItems: ImmutableList<ListModel>,
    modifier: Modifier,
) {
    ListScreen(
        items = listItems,
        modifier = modifier,
    )
}
