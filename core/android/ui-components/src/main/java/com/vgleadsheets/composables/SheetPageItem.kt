package com.vgleadsheets.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.graphics.createBitmap
import com.vgleadsheets.components.SheetPageListModel
import com.vgleadsheets.composables.subs.CrossfadeSheet

@Composable
fun SheetPageItem(
    model: SheetPageListModel,
) {
    CrossfadeSheet(
        imageUrl = model.sheetUrl,
        loadingBitmap = createBitmap(1, 2),
        sheetId = model.dataId,
        onClick = {},
        modifier = Modifier
    )
}
