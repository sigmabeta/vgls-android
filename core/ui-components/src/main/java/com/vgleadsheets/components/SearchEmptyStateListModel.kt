package com.vgleadsheets.components

data class SearchEmptyStateListModel(val stringId: Int = R.string.empty_search_no_query) :
    ListModel {
    override val dataId = stringId.hashCode().toLong()
    override val layoutId = R.layout.list_component_search_empty_state
}
