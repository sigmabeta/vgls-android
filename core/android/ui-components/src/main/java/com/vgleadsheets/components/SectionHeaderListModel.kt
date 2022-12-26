package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import com.vgleadsheets.composables.SectionHeader

data class SectionHeaderListModel(
    val title: String
) : ListModel, ComposableModel {
    override val dataId = title.hashCode().toLong()
    override val layoutId = R.layout.composable_menu_item

    @Composable
    override fun Content() {
        SectionHeader(name = title, menu = true)
    }
}
