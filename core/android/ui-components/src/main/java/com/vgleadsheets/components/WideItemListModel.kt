package com.vgleadsheets.components

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

data class WideItemListModel(
    override val dataId: Long,
    val name: String,
    val imageUrl: String?,
    @DrawableRes val imagePlaceholder: Int,
    val actionableId: Long? = null,
    val onClick: () -> Unit
) : ListModel {
    override val layoutId = this::class.simpleName.hashCode()

    @Composable
    override fun Content(modifier: Modifier) {
        // WideItem(
        //     model = this,
        //     modifier = modifier
        // )
    }
}