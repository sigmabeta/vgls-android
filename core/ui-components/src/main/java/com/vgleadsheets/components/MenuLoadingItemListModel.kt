package com.vgleadsheets.components

data class MenuLoadingItemListModel(
    val name: String,
    val iconId: Int
) : ListModel {
    override val dataId = iconId.toLong()

    override val layoutId = R.layout.list_component_menu_loading
}
