package com.vgleadsheets.remaster.browse

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.ui.list.ListScreen

@Composable
fun BrowseScreen(
    state: State,
    modifier: Modifier
) {
    ListScreen(
        state.items,
        modifier = modifier
    )
}
