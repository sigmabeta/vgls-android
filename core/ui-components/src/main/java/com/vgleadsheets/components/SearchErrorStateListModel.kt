package com.vgleadsheets.components

data class SearchErrorStateListModel(
    val failedOperationName: String,
    val errorString: String,
) : ListModel {

    override val dataId = errorString.hashCode().toLong()
    override val layoutId = R.layout.list_component_search_error_state
}
