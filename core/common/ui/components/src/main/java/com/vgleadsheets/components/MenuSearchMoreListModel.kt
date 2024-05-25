package com.vgleadsheets.components

import com.vgleadsheets.appcomm.VglsAction

data class MenuSearchMoreListModel(
    val text: String,
    val clickAction: VglsAction,
) : ListModel() {
    override val dataId = text.hashCode().toLong()
    override val columns = ListModel.COLUMNS_ALL
}
