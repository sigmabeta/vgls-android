package com.vgleadsheets.components

import com.vgleadsheets.appcomm.VglsAction
import kotlinx.collections.immutable.ImmutableList

data class SheetPageListModel(
    val sourceInfo: Any,
    val title: String,
    val gameName: String,
    val composers: ImmutableList<String>,
    val pageNumber: Int,
    val clickAction: VglsAction,
    override val dataId: Long = sourceInfo.hashCode().toLong()
) : ListModel() {
    override val columns = ListModel.COLUMNS_ALL
}
