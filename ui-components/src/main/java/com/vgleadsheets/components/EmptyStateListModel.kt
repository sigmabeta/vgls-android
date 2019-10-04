package com.vgleadsheets.components

data class EmptyStateListModel(
    val iconId: Int,
    val explanation: String
) : ListModel {
    override val dataId = explanation.hashCode().toLong()
    override val layoutId = R.layout.list_component_empty_state
}
