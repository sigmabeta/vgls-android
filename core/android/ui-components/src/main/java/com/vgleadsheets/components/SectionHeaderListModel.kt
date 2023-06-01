package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.composables.SectionHeader

data class SectionHeaderListModel(
    val title: String
) : ListModel {
    override val dataId = title.hashCode().toLong()
    override val layoutId = R.layout.composable_list_item

    @Composable
    override fun Content(modifier: Modifier) {
        SectionHeader(
            name = title,
            menu = true,
            modifier = modifier
        )
    }
}
