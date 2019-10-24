package com.vgleadsheets.components

data class SheetListModel(
    val sheetUrl: String,
    val listener: ImageListener,
    override val dataId: Long = sheetUrl.hashCode().toLong()
) : ListModel {
    override val layoutId = R.layout.list_component_sheet

    interface ImageListener {
        fun onClicked()
        fun onLoadFailed(imageUrl: String, ex: Exception?)
    }
}
