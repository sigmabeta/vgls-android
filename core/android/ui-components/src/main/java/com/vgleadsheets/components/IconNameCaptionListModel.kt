package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.composables.IconNameCaptionListItem

data class IconNameCaptionListModel(
    override val dataId: Long,
    val name: String,
    val caption: String,
    val iconId: Int,
    val onClick: () -> Unit
) : ListModel {
    override val layoutId = R.layout.list_component_now_playing

    @Composable
    override fun Content(modifier: Modifier) {
        IconNameCaptionListItem(
            model = this,
            modifier = modifier
        )
    }
}
