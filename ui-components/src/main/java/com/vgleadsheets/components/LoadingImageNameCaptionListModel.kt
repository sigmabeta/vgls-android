package com.vgleadsheets.components

data class LoadingImageNameCaptionListModel(
    val loadOperationName: String,
    val loadPositionOffset: Int
) : ListModel {
    override val dataId = loadOperationName.hashCode().toLong() + loadPositionOffset
    override val layoutId = R.layout.list_component_loading_image_name_caption
}
