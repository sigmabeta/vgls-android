package com.vgleadsheets.remaster.composers.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vgleadsheets.ui.list.GridScreen

@Composable
fun ComposersListScreen(
    state: State,
    modifier: Modifier
) {
    GridScreen(
        items = state.listItems,
        modifier = modifier,
        minSize = 160.dp
    )
}
