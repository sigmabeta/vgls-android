package com.vgleadsheets.components

import com.vgleadsheets.perf.tracking.api.PerfTracker

data class MenuTitleBarListModel(
    val title: String,
    val subtitle: String,
    val expanded: Boolean,
    val onClicked: () -> Unit,
    val onChangePartClicked: () -> Unit,
    val screenName: String,
    val tracker: PerfTracker
) : ListModel {

    override val dataId = javaClass.simpleName.hashCode().toLong()

    override val layoutId = R.layout.list_component_menu_title_bar
}