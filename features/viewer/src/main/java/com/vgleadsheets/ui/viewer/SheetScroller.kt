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
import com.vgleadsheets.components.ZoomableSheetPageListModel
import com.vgleadsheets.composables.ZoomableSheetPageItem
import com.vgleadsheets.pdf.ZOOM_MAX_PDF
import kotlinx.collections.immutable.ImmutableList
import me.saket.telephoto.zoomable.ZoomSpec
import me.saket.telephoto.zoomable.ZoomableState
import me.saket.telephoto.zoomable.rememberZoomableState

@Composable
internal fun SheetScroller(
    items: ImmutableList<ZoomableSheetPageListModel>,
    listState: LazyListState,
    actionSink: ActionSink,
    zoomSpec: ZoomSpec = ZoomSpec(maxZoomFactor = ZOOM_MAX_PDF.toFloat()),
    zoomableState: ZoomableState = rememberZoomableState(zoomSpec),
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
            ZoomableSheetPageItem(
                model = it,
                actionSink = actionSink,
                modifier = Modifier.animateItem(),
                padding = PaddingValues(),
                zoomableState = zoomableState,
                zoomSpec = zoomSpec,
            )
        }
    }
}
