package com.vgleadsheets.components

data class NetworkRefreshingListModel(
    val refreshType: String
) : ListModel() {
    override val dataId = refreshType.hashCode().toLong()
    override val columns = ListModel.COLUMNS_ALL
}
