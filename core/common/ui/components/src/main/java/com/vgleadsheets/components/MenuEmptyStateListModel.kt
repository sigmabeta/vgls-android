package com.vgleadsheets.components

data class MenuEmptyStateListModel(
    val iconId: Int,
    val explanation: String,
    val showCrossOut: Boolean = true
) : ListModel() {
    override val dataId = explanation.hashCode().toLong()
    override val columns = ListModel.COLUMNS_ALL
}
