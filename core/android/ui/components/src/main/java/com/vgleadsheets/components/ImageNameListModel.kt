package com.vgleadsheets.components

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.composables.ImageNameListItem
import com.vgleadsheets.ui.components.R

data class ImageNameListModel(
    override val dataId: Long,
    val name: String,
    val imageUrl: String?,
    @DrawableRes val imagePlaceholder: Int,
    val actionableId: Long? = null,
    val onClick: () -> Unit
) : ListModel {
    override val layoutId = R.layout.list_component_image_name_caption
    override val columns = ListModel.COLUMNS_ALL

    @Composable
    override fun Content(modifier: Modifier) {
        ImageNameListItem(
            model = this,
            modifier = modifier
        )
    }
}
