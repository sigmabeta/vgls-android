package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.composables.SheetPageItem
import com.vgleadsheets.ui.components.R

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
    override val layoutId = R.layout.composable_viewpager_item
    override val columns = ListModel.COLUMNS_ALL

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
