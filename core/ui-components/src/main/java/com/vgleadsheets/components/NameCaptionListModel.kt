package com.vgleadsheets.components

data class NameCaptionListModel(
    override val dataId: Long,
    val name: String,
    val caption: String,
    val onClick: () -> Unit,
) : ListModel {
    override val layoutId = R.layout.list_component_name_caption
}
