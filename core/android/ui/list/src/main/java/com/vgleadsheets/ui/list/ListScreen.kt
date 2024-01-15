package com.vgleadsheets.ui.list

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.components.ListModel
import kotlinx.collections.immutable.ImmutableList

@Composable
fun ListScreen(
    items: ImmutableList<ListModel>,
    modifier: Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(
            items = items,
            key = { it.dataId },
            contentType = { it.layoutId }
        ) {
            it.Content(modifier = Modifier)
        }
    }
}
