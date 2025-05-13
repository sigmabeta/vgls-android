package com.vgleadsheets.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.components.SheetPageListModel
import com.vgleadsheets.components.ZoomableSheetPageListModel
import com.vgleadsheets.composables.subs.CrossfadeSheet
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

    ZoomableSheet(
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
        actuallyZoomable = model.actuallyZoomable,
        actionSink = actionSink,
        portrait = portrait,
        modifier = modifier
            .padding(padding),
    )
}
