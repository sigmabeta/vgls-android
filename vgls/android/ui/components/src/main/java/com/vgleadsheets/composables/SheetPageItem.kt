package com.vgleadsheets.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import net.sigmabeta.sage.appcomm.ActionSink
import net.sigmabeta.sage.android.bitmaps.SheetConstants
import net.sigmabeta.sage.components.SheetPageListModel
import net.sigmabeta.sage.components.ZoomableSheetPageListModel
import com.vgleadsheets.composables.subs.CrossfadeSheet
import com.vgleadsheets.composables.utils.ImageSize
import com.vgleadsheets.images.LoadingIndicatorConfig
import me.saket.telephoto.zoomable.ZoomSpec

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
    zoomSpec: ZoomSpec,
    modifier: Modifier,
    padding: PaddingValues,
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
        zoomSpec = zoomSpec,
        actionSink = actionSink,
        modifier = modifier
            .fillMaxSize()
            .padding(padding),
    )
}
