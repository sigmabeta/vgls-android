package com.vgleadsheets.components

data class TitleListModel(
    val title: String,
    val subtitle: String
) : ListModel {
    override val dataId = R.layout.list_component_title.toLong()
    override val layoutId = R.layout.list_component_title
}
