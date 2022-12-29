package com.vgleadsheets.components

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import com.vgleadsheets.composables.ImageNameCaptionListItem

data class SearchResultListModel(
    override val dataId: Long,
    val name: String,
    val caption: String,
    val imageUrl: String?,
    @DrawableRes val imagePlaceholder: Int,
    val actionableId: Long? = null,
    val onClick: () -> Unit
) : ListModel, ComposableModel {
    override val layoutId = R.layout.list_component_search_result

    @Composable
    override fun Content() {
        ImageNameCaptionListItem(model = this)
    }
}
