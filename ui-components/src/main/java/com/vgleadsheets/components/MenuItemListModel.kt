package com.vgleadsheets.components

import com.vgleadsheets.perf.tracking.api.PerfTracker

data class MenuItemListModel(
    val name: String,
    val iconId: Int,
    val handler: EventHandler,
    val screenName: String,
    val tracker: PerfTracker
) : ListModel {
    interface EventHandler {
        fun onClicked(clicked: MenuItemListModel)
    }

    override val dataId = iconId.toLong()

    override val layoutId = R.layout.list_component_menu_item
}
