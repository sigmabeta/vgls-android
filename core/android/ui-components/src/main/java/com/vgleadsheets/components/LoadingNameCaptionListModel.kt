package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.composables.LoadingListItem

data class LoadingNameCaptionListModel(
    val loadOperationName: String,
    val loadPositionOffset: Int
) : ListModel {
    override val dataId = loadOperationName.hashCode().toLong() + loadPositionOffset
    override val layoutId = R.layout.list_component_loading_name_caption

    @Composable
    override fun Content(modifier: Modifier) {
        LoadingListItem(
            withImage = false,
            seed = dataId,
            modifier = modifier
        )
    }
}
