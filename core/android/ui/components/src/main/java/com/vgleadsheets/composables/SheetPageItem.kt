package com.vgleadsheets.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.bitmaps.SheetConstants
import com.vgleadsheets.components.SheetPageListModel
import com.vgleadsheets.components.ZoomableSheetPageListModel
import com.vgleadsheets.composables.subs.CrossfadeSheet
import com.vgleadsheets.composables.utils.ImageSize
import com.vgleadsheets.images.LoadingIndicatorConfig

@Composable
fun SheetPageItem(
    model: SheetPageListModel,
    actionSink: ActionSink,
    showDebug: Boolean,
    modifier: Modifier,
    padding: PaddingValues,
) {
    val contentDescription = "${model.title} from ${model.gameName}, page ${model.pageNumber + 1}"

    CrossfadeSheet(
        pdfConfigById = model.pdfConfigById,
        contentDescription = contentDescription,
        loadingIndicatorConfig = LoadingIndicatorConfig(
            model.title,
            model.gameName,
            model.composers,
            model.pageNumber,
            model.pdfConfigById.pdfSize,
        ),
        sheetId = model.dataId,
        showDebug = showDebug,
        modifier = modifier
            .padding(padding),
    )
}

@Composable
fun ZoomableSheetPageItem(
    model: ZoomableSheetPageListModel,
    actionSink: ActionSink,
    showDebug: Boolean,
    modifier: Modifier,
    padding: PaddingValues,
    portrait: Boolean,
) {
    val contentDescription = "${model.title} from ${model.gameName}, page ${model.pageNumber + 1}"

    val heightDp = ImageSize.LARGE_HEIGHT.size
    val (maxWidthPx, maxHeightPx) = with(LocalDensity.current) {
        heightDp.toPx() * SheetConstants.ASPECT_RATIO to heightDp.toPx()
    }

    ZoomableSheet(
        pdfConfigById = model.pdfConfigById,
        contentDescription = contentDescription,
        loadingIndicatorConfig = LoadingIndicatorConfig(
            model.title,
            model.gameName,
            model.composers,
            model.pageNumber,
            model.pdfConfigById.pdfSize,
            maxWidth = maxWidthPx.toInt(),
            maxHeight = maxHeightPx.toInt(),
        ),
        sheetId = model.dataId,
        showDebug = showDebug,
        actuallyZoomable = model.actuallyZoomable,
        actionSink = actionSink,
        portrait = portrait,
        modifier = modifier
            .wrapContentSize()
            .padding(padding),
    )
}
