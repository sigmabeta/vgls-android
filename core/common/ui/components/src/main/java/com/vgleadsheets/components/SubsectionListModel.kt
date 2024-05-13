package com.vgleadsheets.components

data class SubsectionListModel(
    val id: Long,
    val titleModel: SubsectionHeaderListModel,
    val children: List<ListModel>
) : ListModel() {
    override val dataId = titleModel.hashCode().toLong()
    override val columns = ListModel.COLUMNS_ALL
}
