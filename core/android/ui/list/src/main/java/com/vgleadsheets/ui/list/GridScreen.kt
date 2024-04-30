package com.vgleadsheets.ui.list

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vgleadsheets.components.ListModel
import kotlinx.collections.immutable.ImmutableList

@Composable
fun GridScreen(
    items: ImmutableList<ListModel>,
    modifier: Modifier,
    minSize: Dp = 128.dp
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize),
        modifier = modifier
    ) {
        items(
            items = items,
            key = { it.dataId },
            contentType = { it.layoutId },
            span = {
                if (it.columns < 1) {
                    GridItemSpan(maxLineSpan)
                } else {
                    GridItemSpan(it.columns)
                }
            }
        ) {
            it.Content(modifier = Modifier)
        }
    }
}
