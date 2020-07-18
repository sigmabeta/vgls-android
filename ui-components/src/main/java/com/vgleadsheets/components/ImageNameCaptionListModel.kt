package com.vgleadsheets.components

import com.vgleadsheets.PerfHandler

data class ImageNameCaptionListModel(
    override val dataId: Long,
    val name: String,
    val caption: String,
    val imageUrl: String?,
    val imagePlaceholder: Int,
    val handler: EventHandler,
    val actionableId: Long? = null,
    val perfHandler: PerfHandler
) : ListModel {
    interface EventHandler {
        fun onClicked(clicked: ImageNameCaptionListModel)
        fun clearClicked()
    }

    override val layoutId = R.layout.list_component_image_name_caption
}
