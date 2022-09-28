package com.vgleadsheets.components

data class MenuSectionHeaderListModel(
    val title: String
) : ListModel {
    override val dataId = title.hashCode().toLong()
    override val layoutId = R.layout.list_component_menu_section_header
}
