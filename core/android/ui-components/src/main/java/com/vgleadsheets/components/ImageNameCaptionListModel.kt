package com.vgleadsheets.components

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.composables.ImageNameCaptionListItem

data class ImageNameCaptionListModel(
    override val dataId: Long,
    val name: String,
    val caption: String,
    val imageUrl: String?,
    @DrawableRes val imagePlaceholder: Int,
    val actionableId: Long? = null,
    val onClick: () -> Unit
) : ListModel, ComposableModel {
    override val layoutId = R.layout.composable_list_item

    @Composable
    override fun Content(modifier: Modifier) {
        ImageNameCaptionListItem(
            model = this,
            modifier = modifier
        )
    }
}
