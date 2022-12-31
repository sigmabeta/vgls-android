package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.composables.SectionHeader

data class MenuSectionHeaderListModel(
    val title: String
) : ListModel, ComposableModel {
    override val dataId = title.hashCode().toLong()
    override val layoutId = R.layout.list_component_menu_section_header

    @Composable
    override fun Content(modifier: Modifier) {
        SectionHeader(
            name = title,
            menu = true,
            modifier = modifier
        )
    }
}
