package com.vgleadsheets.components

import androidx.annotation.DrawableRes

data class ImageNameCaptionListModel(
    override val dataId: Long,
    val name: String,
    val caption: String,
    val imageUrl: String?,
    @DrawableRes val imagePlaceholder: Int,
    val actionableId: Long? = null,
    val onClick: () -> Unit
) : ListModel {
    override val layoutId = R.layout.list_component_image_name_caption
}
