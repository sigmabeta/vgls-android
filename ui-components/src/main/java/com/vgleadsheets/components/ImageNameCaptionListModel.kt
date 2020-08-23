package com.vgleadsheets.components

import com.vgleadsheets.perf.tracking.api.PerfTracker

data class ImageNameCaptionListModel(
    override val dataId: Long,
    val name: String,
    val caption: String,
    val imageUrl: String?,
    val imagePlaceholder: Int,
    val handler: EventHandler,
    val actionableId: Long? = null,
    val screenName: String,
    val tracker: PerfTracker
) : ListModel {
    interface EventHandler {
        fun onClicked(clicked: ImageNameCaptionListModel)
        fun clearClicked()
    }

    override val layoutId = R.layout.list_component_image_name_caption
}
