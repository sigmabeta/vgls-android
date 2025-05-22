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
import com.vgleadsheets.pdf.ZOOM_MAX_PDF
import kotlinx.collections.immutable.ImmutableList
import me.saket.telephoto.zoomable.ZoomSpec
import me.saket.telephoto.zoomable.ZoomableState
import me.saket.telephoto.zoomable.rememberZoomableState

@Composable
internal fun BoxScope.SheetPager(
    items: ImmutableList<ZoomableSheetPageListModel>,
    pagerState: PagerState,
    actionSink: ActionSink,
    zoomSpec: ZoomSpec = ZoomSpec(maxZoomFactor = ZOOM_MAX_PDF.toFloat()),
    zoomableState: ZoomableState = rememberZoomableState(zoomSpec),
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
        println("scroller: $item")

        ZoomableSheetPageItem(
            model = item,
            actionSink = actionSink,
            modifier = Modifier.fillMaxHeight(),
            padding = PaddingValues(),
            zoomableState = zoomableState,
            zoomSpec = zoomSpec,
        )
    }
}
