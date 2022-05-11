package com.vgleadsheets.components

data class ErrorStateListModel(
    val failedOperationName: String,
    val errorString: String,
) : ListModel {

    override val dataId = errorString.hashCode().toLong()
    override val layoutId = R.layout.list_component_error_state
}
