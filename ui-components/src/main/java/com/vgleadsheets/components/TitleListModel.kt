package com.vgleadsheets.components

import com.vgleadsheets.PerfHandler

data class TitleListModel(
    val title: String,
    val subtitle: String,
    val photoUrl: String? = null,
    val placeholder: Int? = R.drawable.ic_logo,
    val perfHandler: PerfHandler
) : ListModel {
    override val dataId = R.layout.list_component_title.toLong()
    override val layoutId = R.layout.list_component_title
}
