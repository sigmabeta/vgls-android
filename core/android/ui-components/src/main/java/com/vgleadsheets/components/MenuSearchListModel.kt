package com.vgleadsheets.components

data class MenuSearchListModel(
    val searchQuery: String?,
    val onTextEntered: (String) -> Unit,
    val onMenuButtonClick: () -> Unit,
    val onClearClick: () -> Unit
) : ListModel {

    override val dataId = javaClass.simpleName.hashCode().toLong()

    override val layoutId = R.layout.list_component_menu_search_bar
}
