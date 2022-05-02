package com.vgleadsheets.components

data class ToolbarItemListModel(
    val name: String,
    val iconId: Int,
    val handler: EventHandler
) : ListModel {
    override val dataId = iconId.toLong()

    override val layoutId = R.layout.list_component_toolbar_item

    interface EventHandler {
        fun onClicked(clicked: ToolbarItemListModel)
        fun onLongClicked(clicked: ToolbarItemListModel)
        fun clearClicked()
    }
}
