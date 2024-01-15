package com.vgleadsheets.components

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.composables.WideItem

data class WideItemListModel(
    override val dataId: Long,
    val name: String,
    val imageUrl: String?,
    @DrawableRes val imagePlaceholder: Int,
    val actionableId: Long? = null,
    val onClick: () -> Unit
) : ListModel {
    override val layoutId = this::class.simpleName.hashCode()
    override val columns = 1

    @Composable
    override fun Content(modifier: Modifier) {
        WideItem(
            model = this,
            modifier = modifier
        )
    }
}
