package com.vgleadsheets.components

import com.vgleadsheets.perf.tracking.api.PerfTracker

data class IconNameCaptionListModel(
    override val dataId: Long,
    val name: String,
    val caption: String,
    val iconId: Int,
    val screenName: String,
    val tracker: PerfTracker,
    val clickHandler: () -> Unit
) : ListModel {
    override val layoutId = R.layout.list_component_icon_name_caption
}
