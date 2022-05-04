package com.vgleadsheets.components

data class SectionHeaderListModel(
    val title: String
) : ListModel {
    override val dataId = title.hashCode().toLong()
    override val layoutId = R.layout.list_component_section_header
}
