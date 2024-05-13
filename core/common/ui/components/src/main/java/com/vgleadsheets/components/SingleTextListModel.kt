package com.vgleadsheets.components

data class SingleTextListModel(
    override val dataId: Long,
    val name: String,
    val onClick: () -> Unit,
) : ListModel() {
    override val columns = ListModel.COLUMNS_ALL
}
