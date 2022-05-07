package com.vgleadsheets.components

import com.vgleadsheets.perf.tracking.api.PerfTracker

data class EikonNameCaptionListModel(
    override val dataId: Long,
    val name: String,
    val caption: String,
    val eikonId: Int,
    val screenName: String,
    val tracker: PerfTracker
) : ListModel {
    override val layoutId = R.layout.list_component_eikon_name_caption
}
