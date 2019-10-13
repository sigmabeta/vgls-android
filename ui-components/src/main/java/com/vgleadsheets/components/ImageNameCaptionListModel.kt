package com.vgleadsheets.components

data class ImageNameCaptionListModel(
    override val dataId: Long,
    val name: String,
    val caption: String,
    val imageUrl: String?,
    val imagePlaceholder: Int,
    val handler: EventHandler
) : ListModel {
    interface EventHandler {
        fun onClicked(clicked: ImageNameCaptionListModel)
    }

    override val layoutId = R.layout.list_component_image_name_caption
}
