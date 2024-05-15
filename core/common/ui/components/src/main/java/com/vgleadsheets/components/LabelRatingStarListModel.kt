package com.vgleadsheets.components

import com.vgleadsheets.state.VglsAction

data class LabelRatingStarListModel(
    val label: String,
    val value: Int,
    val clickAction: VglsAction,
    override val dataId: Long = label.hashCode().toLong()
) : ListModel() {
    override val columns = ListModel.COLUMNS_ALL
}
