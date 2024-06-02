package com.vgleadsheets.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.components.SheetPageListModel
import com.vgleadsheets.composables.subs.CrossfadeSheet
import com.vgleadsheets.images.PagePreview

@Composable
fun SheetPageItem(
    model: SheetPageListModel,
    modifier: Modifier,
) {
    CrossfadeSheet(
        sourceInfo = model.sourceInfo,
        pagePreview = PagePreview(
            model.title,
            model.transposition,
            model.gameName,
            model.composers,
            model.pageNumber,
        ),
        sheetId = model.dataId,
        modifier = modifier
    )
}
