package com.vgleadsheets.ui.list

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.vgleadsheets.components.ListModel
import kotlinx.collections.immutable.ImmutableList

@Composable
fun ListScreen(
    title: String?,
    items: ImmutableList<ListModel>,
    titleUpdater: (String?) -> Unit,
    modifier: Modifier
) {
    if (title != null) {
        LaunchedEffect(Unit) {
            titleUpdater(title)
        }
    }

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
