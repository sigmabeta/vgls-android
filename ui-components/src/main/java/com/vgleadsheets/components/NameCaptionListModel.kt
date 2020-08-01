package com.vgleadsheets.components

data class NameCaptionListModel(
    override val dataId: Long,
    val name: String,
    val caption: String,
    val handler: EventHandler,
    val screenName: String,
    val tracker: com.vgleadsheets.perf.tracking.api.PerfTracker
) : ListModel {
    override val layoutId = R.layout.list_component_name_caption

    interface EventHandler {
        fun onClicked(clicked: NameCaptionListModel)
        fun clearClicked()
    }
}
