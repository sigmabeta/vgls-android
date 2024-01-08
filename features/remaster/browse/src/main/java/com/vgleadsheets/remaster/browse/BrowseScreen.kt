package com.vgleadsheets.remaster.browse

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BrowseScreen(
    state: BrowseState
) {
    LazyColumn {
        items(
            items = state.items,
            key = { it.dataId },
            contentType = { it.layoutId }
        ) {
            it.Content(modifier = Modifier)
        }
    }
}
