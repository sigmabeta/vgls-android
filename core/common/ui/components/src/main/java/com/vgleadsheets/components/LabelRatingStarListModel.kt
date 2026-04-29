package com.vgleadsheets.components

import net.sigmabeta.sage.appcomm.VglsAction

data class LabelRatingStarListModel(
    val label: String,
    val value: Int,
    val clickAction: VglsAction,
    override val dataId: Long = label.hashCode().toLong()
) : ListModel() {
    override val columns = ListModel.COLUMNS_ALL
}
