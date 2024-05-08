package com.vgleadsheets.remaster.composers.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.ui.list.GridScreen
import kotlinx.collections.immutable.ImmutableList

@Composable
fun ComposersListScreen(
    listItems: ImmutableList<ListModel>,
    modifier: Modifier
) {
    GridScreen(
        items = listItems,
        modifier = modifier,
        minSize = 160.dp
    )
}
