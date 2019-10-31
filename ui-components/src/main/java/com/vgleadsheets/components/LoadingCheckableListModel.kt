package com.vgleadsheets.components

data class LoadingCheckableListModel(
    val listPosition: Int
) : ListModel {
    override val dataId = Long.MAX_VALUE - listPosition
    override val layoutId = R.layout.list_component_checkable_loading
}
