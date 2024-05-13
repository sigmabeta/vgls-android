package com.vgleadsheets.components

data class SectionHeaderListModel(
    val title: String
) : ListModel() {
    override val dataId = title.hashCode().toLong()
    override val columns = ListModel.COLUMNS_ALL
}
