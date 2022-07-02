package com.vgleadsheets.components

data class SingleTextListModel(
    override val dataId: Long,
    val name: String,
    val onClick: () -> Unit,
) : ListModel {
    override val layoutId = R.layout.list_component_single_line
}
