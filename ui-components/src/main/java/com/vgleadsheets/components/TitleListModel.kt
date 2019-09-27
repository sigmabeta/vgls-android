package com.vgleadsheets.components

data class TitleListModel(
    override val dataId: Long,
    val title: String,
    val subtitle: String,
    override val layoutId: Int = R.layout.list_component_title
) : ListModel
