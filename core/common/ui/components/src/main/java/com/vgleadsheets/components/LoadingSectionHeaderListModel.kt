package com.vgleadsheets.components

data class LoadingSectionHeaderListModel(
    val loadOperationName: String,
    val loadPositionOffset: Int
) : ListModel() {
    override val dataId = loadOperationName.hashCode().toLong() + loadPositionOffset
    override val columns = ListModel.COLUMNS_ALL
}
