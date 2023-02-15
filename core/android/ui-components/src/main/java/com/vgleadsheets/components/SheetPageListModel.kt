package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.IsComposeEnabled
import com.vgleadsheets.composables.SheetPageItem

data class SheetPageListModel(
    val sheetUrl: String,
    val pageNumber: Int,
    val title: String,
    val transposition: String,
    val gameName: String,
    val composers: List<String>,
    val songId: Long, // NOT the id of this page!
    val listener: ImageListener,
    override val dataId: Long = sheetUrl.hashCode().toLong()
) : ListModel {
    override val layoutId = if (IsComposeEnabled.WELL_IS_IT) {
        R.layout.composable_viewpager_item
    } else {
        R.layout.list_component_sheet
    }

    interface ImageListener {
        fun onClicked()
        fun onLoadStarted()
        fun onLoadComplete()
        fun onLoadFailed(imageUrl: String, ex: Throwable?)
    }

    @Composable
    override fun Content(modifier: Modifier) {
        SheetPageItem(
            model = this,
        )
    }
}
