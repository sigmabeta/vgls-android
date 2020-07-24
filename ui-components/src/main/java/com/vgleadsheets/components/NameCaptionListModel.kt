package com.vgleadsheets.components

import com.vgleadsheets.perf.tracking.common.PerfTracker

data class NameCaptionListModel(
    override val dataId: Long,
    val name: String,
    val caption: String,
    val handler: EventHandler,
    val screenName: String,
    val tracker: PerfTracker
) : ListModel {
    override val layoutId = R.layout.list_component_name_caption

    interface EventHandler {
        fun onClicked(clicked: NameCaptionListModel)
        fun clearClicked()
    }
}
