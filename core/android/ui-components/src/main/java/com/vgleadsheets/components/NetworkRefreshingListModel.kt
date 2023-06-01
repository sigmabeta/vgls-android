package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.composables.LoadingListItem

data class NetworkRefreshingListModel(
    val refreshType: String
) : ListModel {
    override val dataId = refreshType.hashCode().toLong()
    override val layoutId = R.layout.composable_list_item

    @Composable
    override fun Content(modifier: Modifier) {
        LoadingListItem(
            withImage = true,
            seed = dataId,
            modifier = modifier,
        )
    }
}
