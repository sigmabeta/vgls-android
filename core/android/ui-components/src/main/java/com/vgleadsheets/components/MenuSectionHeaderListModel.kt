package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import com.vgleadsheets.composables.SectionHeader

data class MenuSectionHeaderListModel(
    val title: String
) : ListModel, ComposableModel {
    override val dataId = title.hashCode().toLong()
    override val layoutId = R.layout.list_component_menu_section_header

    @Composable
    override fun Content() {
        SectionHeader(name = title, menu = true)
    }
}
