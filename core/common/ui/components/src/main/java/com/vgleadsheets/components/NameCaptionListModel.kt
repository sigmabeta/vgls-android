package com.vgleadsheets.components

data class NameCaptionListModel(
    override val dataId: Long,
    val name: String,
    val caption: String,
    val onClick: () -> Unit,
) : ListModel() {
    override val columns = ListModel.COLUMNS_ALL
}
