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
import com.vgleadsheets.components.ZoomableSheetPageListModel
import com.vgleadsheets.composables.ZoomableSheetPageItem
import kotlinx.collections.immutable.ImmutableList
import me.saket.telephoto.zoomable.ZoomSpec

@Composable
internal fun BoxScope.SheetPager(
    items: ImmutableList<ZoomableSheetPageListModel>,
    zoomSpec: ZoomSpec,
    pagerState: PagerState,
    allowPaging: Boolean,
    actionSink: ActionSink,
) {
    if (items.isEmpty()) {
        // To fill the screen and prevent janky animation
        Box(modifier = Modifier)
        return
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.align(Alignment.Center),
        userScrollEnabled = allowPaging,
    ) { page ->
        val item = items[page]
        ZoomableSheetPageItem(
            model = item,
            zoomSpec = zoomSpec,
            actionSink = actionSink,
            modifier = Modifier.fillMaxHeight(),
            padding = PaddingValues(),
        )
    }
}
