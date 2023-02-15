package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.composables.NameCaptionListItem

data class NameCaptionListModel(
    override val dataId: Long,
    val name: String,
    val caption: String,
    val onClick: () -> Unit,
) : ListModel {
    override val layoutId = R.layout.list_component_name_caption

    @Composable
    override fun Content(modifier: Modifier) {
        NameCaptionListItem(
            model = this,
            modifier = modifier
        )
    }
}
