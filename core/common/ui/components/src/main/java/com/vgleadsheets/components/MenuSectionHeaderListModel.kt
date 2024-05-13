package com.vgleadsheets.components

data class MenuSectionHeaderListModel(
    val title: String
) : ListModel() {
    override val dataId = title.hashCode().toLong()
    override val columns = ListModel.COLUMNS_ALL
}
