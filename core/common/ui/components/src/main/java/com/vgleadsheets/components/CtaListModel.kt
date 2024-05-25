package com.vgleadsheets.components

import com.vgleadsheets.appcomm.VglsAction

data class CtaListModel(
    val iconId: Int,
    val name: String,
    val clickAction: VglsAction,
) : ListModel() {
    override val dataId = name.hashCode().toLong()
    override val columns = ListModel.COLUMNS_ALL
}
