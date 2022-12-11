package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import com.vgleadsheets.composables.LoadingListItem

data class LoadingImageNameCaptionListModel(
    val loadOperationName: String,
    val loadPositionOffset: Int
) : ListModel, ComposableModel  {
    override val dataId = loadOperationName.hashCode().toLong() + loadPositionOffset
    override val layoutId = R.layout.composable_list_item
    @Composable
    override fun Content() {
        LoadingListItem(dataId)
    }
}
