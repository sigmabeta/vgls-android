package com.vgleadsheets.components

import com.vgleadsheets.appcomm.VglsAction

data class LabelValueListModel(
    val label: String,
    val value: String,
    val clickAction: VglsAction,
    override val dataId: Long = label.hashCode().toLong()
) : ListModel() {
    override val columns = ListModel.COLUMNS_ALL
}
