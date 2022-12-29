package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import com.vgleadsheets.composables.IconNameCaptionListItem

data class IconNameCaptionListModel(
    override val dataId: Long,
    val name: String,
    val caption: String,
    val iconId: Int,
    val onClick: () -> Unit
) : ListModel, ComposableModel {
    override val layoutId = R.layout.list_component_now_playing

    @Composable
    override fun Content() {
        IconNameCaptionListItem(model = this)
    }
}
