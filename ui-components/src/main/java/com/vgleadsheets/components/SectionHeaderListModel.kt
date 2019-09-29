package com.vgleadsheets.components

data class SectionHeaderListModel(
    override val dataId: Long,
    val title: String,
    override val layoutId: Int = R.layout.list_component_section_header
) : ListModel
