package com.vgleadsheets.components

data class IconNameCaptionListModel(
    override val dataId: Long,
    val name: String,
    val caption: String,
    val iconId: Int,
    val onClick: () -> Unit
) : ListModel {
    override val layoutId = R.layout.list_component_icon_name_caption
}
