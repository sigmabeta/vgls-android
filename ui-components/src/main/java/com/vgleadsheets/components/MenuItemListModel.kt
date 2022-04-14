package com.vgleadsheets.components

import com.vgleadsheets.perf.tracking.api.PerfTracker

data class MenuItemListModel(
    val name: String,
    val caption: String?,
    val iconId: Int,
    val onClicked: () -> Unit,
    val screenName: String,
    val tracker: PerfTracker
) : ListModel {
    override val dataId = iconId.toLong()

    override val layoutId = R.layout.list_component_menu_item
}
