package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.composables.SubsectionHeader
import com.vgleadsheets.ui.components.R

data class SubsectionHeaderListModel(
    val title: String
) : ListModel {
    override val dataId = title.hashCode().toLong()
    override val layoutId = R.layout.composable_list_item

    @Composable
    override fun Content(modifier: Modifier) {
        SubsectionHeader(
            model = this,
            modifier = modifier
        )
    }
}
