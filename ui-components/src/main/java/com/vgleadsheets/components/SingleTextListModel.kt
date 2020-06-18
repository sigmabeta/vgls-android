package com.vgleadsheets.components

data class SingleTextListModel(
    override val dataId: Long,
    val name: String,
    val handler: EventHandler
) : ListModel {
    override val layoutId = R.layout.list_component_single_line
    interface EventHandler {
        fun onClicked(clicked: SingleTextListModel)
        fun clearClickedSingleTextModel()
    }
}
