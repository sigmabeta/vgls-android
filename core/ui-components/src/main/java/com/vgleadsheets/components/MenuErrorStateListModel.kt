package com.vgleadsheets.components

data class MenuErrorStateListModel(
    val failedOperationName: String,
    val errorString: String,
) : ListModel {

    override val dataId = errorString.hashCode().toLong()
    override val layoutId = R.layout.list_component_menu_error_state
}
