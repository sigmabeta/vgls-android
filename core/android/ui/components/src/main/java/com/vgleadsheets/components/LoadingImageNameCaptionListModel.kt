package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.composables.LoadingListItem
import com.vgleadsheets.ui.components.R

data class LoadingImageNameCaptionListModel(
    val loadOperationName: String,
    val loadPositionOffset: Int
) : ListModel {
    override val dataId = loadOperationName.hashCode().toLong() + loadPositionOffset
    override val layoutId = R.layout.list_component_loading_image_name_caption
    override val columns = ListModel.COLUMNS_ALL

    @Composable
    override fun Content(modifier: Modifier) {
        LoadingListItem(
            withImage = true,
            seed = dataId,
            modifier = modifier
        )
    }
}
