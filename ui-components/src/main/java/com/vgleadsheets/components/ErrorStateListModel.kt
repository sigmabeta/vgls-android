package com.vgleadsheets.components

data class ErrorStateListModel(
    val errorString: String
) : ListModel {
    override val dataId = errorString.hashCode().toLong()
    override val layoutId = R.layout.list_component_error_state
}
