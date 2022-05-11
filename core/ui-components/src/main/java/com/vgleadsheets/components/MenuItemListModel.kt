package com.vgleadsheets.components

data class MenuItemListModel(
    val name: String,
    val caption: String?,
    val iconId: Int,
    val onClicked: () -> Unit
) : ListModel {
    override val dataId = name.hashCode().toLong()

    override val layoutId = R.layout.list_component_menu_item
}
