package com.vgleadsheets.components

data class LabelValueListModel(
    val label: String,
    val value: String,
    val onClick: () -> Unit,
    override val dataId: Long = label.hashCode().toLong()
) : ListModel() {
    override val columns = ListModel.COLUMNS_ALL
}
