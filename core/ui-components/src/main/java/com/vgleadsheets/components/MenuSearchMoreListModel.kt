package com.vgleadsheets.components

data class MenuSearchMoreListModel(
    val text: String
) : ListModel {
    override val dataId = text.hashCode().toLong()
    override val layoutId = R.layout.list_component_menu_search_more
}
