package com.vgleadsheets.components

data class TitleListModel(
    val title: String,
    val subtitle: String,
    val photoUrl: String? = null,
    val placeholder: Int? = R.drawable.ic_logo
) : ListModel {
    override val dataId = R.layout.list_component_title.toLong()
    override val layoutId = R.layout.list_component_title
}
