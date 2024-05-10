package com.vgleadsheets.remaster.games.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.ui.list.GridScreen
import kotlinx.collections.immutable.ImmutableList

@Composable
fun GamesListScreen(
    title: String?,
    listItems: ImmutableList<ListModel>,
    titleUpdater: (String?) -> Unit,
    modifier: Modifier,
) {
    GridScreen(
        title = title,
        items = listItems,
        titleUpdater = titleUpdater,
        modifier = modifier,
        minSize = 160.dp
    )
}
