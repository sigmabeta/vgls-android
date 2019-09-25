package com.vgleadsheets.components

data class NameCaptionListModel(
    override val dataId: Long,
    val name: String,
    val caption: String,
    override val layoutId: Int = R.layout.list_component_name_caption
    ): ListModel
