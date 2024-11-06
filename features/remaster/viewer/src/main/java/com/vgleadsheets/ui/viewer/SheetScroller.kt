package com.vgleadsheets.ui.viewer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.components.SheetPageListModel
import com.vgleadsheets.composables.Content
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun SheetScroller(
    items: ImmutableList<SheetPageListModel>,
    listState: LazyListState,
    showDebug: Boolean,
    actionSink: ActionSink,
) {
    if (items.isEmpty()) {
        // To fill the screen and prevent janky animation
        Box(modifier = Modifier)
        return
    }

    LazyRow(
        state = listState,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            items = items,
            key = { it.dataId },
            contentType = { it.layoutId() }
        ) {
            it.Content(
                sink = actionSink,
                mod = Modifier.animateItem(),
                debug = showDebug,
                pad = PaddingValues()
            )
        }
    }
}
