package com.vgleadsheets.components

import kotlinx.collections.immutable.ImmutableList

data class SheetPageListModel(
    val sheetUrl: String,
    val pageNumber: Int,
    val title: String,
    val transposition: String,
    val gameName: String,
    val composers: ImmutableList<String>,
    val songId: Long, // NOT the id of this page!
    val listener: ImageListener,
    override val dataId: Long = sheetUrl.hashCode().toLong()
) : ListModel() {
    override val columns = ListModel.COLUMNS_ALL

    interface ImageListener {
        fun onClicked()
        fun onLoadStarted()
        fun onLoadComplete()
        fun onLoadFailed(imageUrl: String, ex: Throwable?)
    }
}
