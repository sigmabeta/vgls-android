package com.vgleadsheets.ui.viewer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.components.SheetPageListModel
import com.vgleadsheets.composables.Content
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun BoxScope.SheetPager(
    items: ImmutableList<SheetPageListModel>,
    pagerState: PagerState,
    showDebug: Boolean,
    actionSink: ActionSink,
) {
    if (items.isEmpty()) {
        // To fill the screen and prevent janky animation
        Box(modifier = Modifier)
        return
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.align(Alignment.Center)
    ) { page ->
        val item = items[page]
        item.Content(
            sink = actionSink,
            mod = Modifier.fillMaxHeight(),
            debug = showDebug,
            pad = PaddingValues()
        )
    }
}
