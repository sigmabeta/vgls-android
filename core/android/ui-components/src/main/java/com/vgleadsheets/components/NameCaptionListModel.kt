package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import com.vgleadsheets.composables.NameCaptionListItem

data class NameCaptionListModel(
    override val dataId: Long,
    val name: String,
    val caption: String,
    val onClick: () -> Unit,
) : ListModel, ComposableModel {
    override val layoutId = R.layout.composable_list_item

    @Composable
    override fun Content() {
        NameCaptionListItem(model = this)
    }
}
