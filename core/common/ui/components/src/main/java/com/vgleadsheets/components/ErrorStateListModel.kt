package com.vgleadsheets.components

data class ErrorStateListModel(
    val failedOperationName: String,
    val errorString: String,
) : ListModel() {
    override val dataId = errorString.hashCode().toLong()
    override val columns = ListModel.COLUMNS_ALL
}
