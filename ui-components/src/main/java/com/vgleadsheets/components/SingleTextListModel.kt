package com.vgleadsheets.components

data class SingleTextListModel(
    override val dataId: Long,
    val name: String,
    val handler: Handler
) : ListModel {
    override val layoutId = R.layout.list_component_single_line
    interface Handler {
        fun onClicked(clicked: SingleTextListModel)
    }
}
