package com.vgleadsheets.components

data class SheetListModel(
    val sheetUrl: String,
    val status: Status,
    val listener: ImageListener,
    override val dataId: Long = sheetUrl.hashCode().toLong()
) : ListModel {
    override val layoutId = R.layout.list_component_sheet

    interface ImageListener {
        fun onClicked(clicked: SheetListModel)
        fun onLoadStart(imageUrl: String)
        fun onLoadSuccess(imageUrl: String)
        fun onLoadFailed(imageUrl: String, ex: Exception?)
    }

    enum class Status {
        NONE,
        LOADING,
        ERROR,
        SUCCESS
    }
}
