package com.vgleadsheets.components

data class CtaListModel(
    val iconId: Int,
    val name: String,
    val onClick: () -> Unit,
) : ListModel() {
    override val dataId = name.hashCode().toLong()
    override val columns = ListModel.COLUMNS_ALL
}
