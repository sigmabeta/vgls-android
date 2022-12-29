package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import com.vgleadsheets.composables.LoadingListItem

data class LoadingImageNameCaptionListModel(
    val loadOperationName: String,
    val loadPositionOffset: Int
) : ListModel, ComposableModel {
    override val dataId = loadOperationName.hashCode().toLong() + loadPositionOffset
    override val layoutId = R.layout.list_component_loading_image_name_caption

    @Composable
    override fun Content() {
        LoadingListItem(
            withImage = true,
            seed = dataId
        )
    }
}
