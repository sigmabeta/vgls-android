package com.vgleadsheets.components

data class TitleListModel(
    override val dataId: Long,
    val title: String,
    val subtitle: String
) : ListModel {
    override val layoutId = R.layout.list_component_title
}
