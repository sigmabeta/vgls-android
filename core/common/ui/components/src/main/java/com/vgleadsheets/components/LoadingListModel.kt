package com.vgleadsheets.components

data class LoadingListModel(
    val withImage: Boolean,
    val withCaption: Boolean,
    val loadOperationName: String,
    val loadPositionOffset: Int
) : ListModel() {
    override val dataId = loadOperationName.hashCode().toLong() + loadPositionOffset
    override val columns = ListModel.COLUMNS_ALL
}
