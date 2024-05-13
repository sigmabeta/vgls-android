package com.vgleadsheets.components

data class MenuTitleBarListModel(
    val partLabel: String,
    val iconId: Int,
    val onSearchButtonClick: () -> Unit,
    val onMenuButtonClick: () -> Unit,
    val onChangePartClick: () -> Unit
) : ListModel() {
    override val dataId = javaClass.simpleName.hashCode().toLong()
    override val columns = ListModel.COLUMNS_ALL
}
