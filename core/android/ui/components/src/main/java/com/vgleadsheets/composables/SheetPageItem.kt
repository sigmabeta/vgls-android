package com.vgleadsheets.composables

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.components.SheetPageListModel
import com.vgleadsheets.composables.subs.CrossfadeSheet
import com.vgleadsheets.images.PagePreview

@Composable
fun SheetPageItem(
    model: SheetPageListModel,
) {
    CrossfadeSheet(
        sourceInfo = model.sheetUrl,
        pagePreview = PagePreview(
            model.title,
            model.transposition,
            model.gameName,
            model.composers
        ),
        sheetId = model.dataId,
        eventListener = model.listener,
        modifier = Modifier.clickable { model.listener.onClicked() }
    )
}
