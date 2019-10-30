package com.vgleadsheets.components

data class CheckableListModel(
    override val dataId: Long,
    val name: String,
    val checked: Boolean,
    val handler: EventHandler
) : ListModel {
    interface EventHandler {
        fun onClicked(clicked: CheckableListModel)
    }

    override val layoutId = R.layout.list_component_checkable
}
