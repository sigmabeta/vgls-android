package com.vgleadsheets.components

import kotlinx.collections.immutable.ImmutableList

data class SubsectionListModel(
    val id: Long,
    val titleModel: SubsectionHeaderListModel,
    val children: ImmutableList<ListModel>
) : ListModel() {
    override val dataId = titleModel.hashCode().toLong()
    override val columns = ListModel.COLUMNS_ALL
}
