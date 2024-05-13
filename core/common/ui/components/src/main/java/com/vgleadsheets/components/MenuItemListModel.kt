package com.vgleadsheets.components

import com.vgleadsheets.ui.Icon

data class MenuItemListModel(
    val name: String,
    val caption: String?,
    val icon: Icon,
    val onClick: () -> Unit,
    val selected: Boolean = false
) : ListModel() {
    override val dataId = name.hashCode().toLong()
    override val columns = COLUMNS_ALL
}
