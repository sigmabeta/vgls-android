package com.vgleadsheets.components

data class LoadingNameCaptionListModel(
    val listPosition: Int
) : ListModel {
    override val dataId = Long.MAX_VALUE - listPosition
    override val layoutId = R.layout.list_component_loading_name_caption
}
