package com.vgleadsheets.components

data class IconNameCaptionListModel(
    override val dataId: Long,
    val name: String,
    val caption: String,
    val iconId: Int,
    val onClick: () -> Unit
) : ListModel() {
    override val columns = ListModel.COLUMNS_ALL
}
