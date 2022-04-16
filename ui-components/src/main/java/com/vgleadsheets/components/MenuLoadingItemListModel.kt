package com.vgleadsheets.components

import com.vgleadsheets.perf.tracking.api.PerfTracker

data class MenuLoadingItemListModel(
    val name: String,
    val iconId: Int,
    val screenName: String,
    val tracker: PerfTracker
) : ListModel {
    override val dataId = iconId.toLong()

    override val layoutId = R.layout.list_component_menu_loading
}