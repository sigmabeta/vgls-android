package com.vgleadsheets.components

data class SheetListModel(
    val sheetUrl: String,
    val status: Status,
    val listener: ImageListener,
    override val dataId: Long = sheetUrl.hashCode().toLong(),
    override val layoutId: Int = R.layout.list_component_sheet
) : ListModel {
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
