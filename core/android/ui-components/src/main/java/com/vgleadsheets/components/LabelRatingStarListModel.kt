package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.composables.LabelRatingListItem

data class LabelRatingStarListModel(
    val label: String,
    val value: Int,
    val onClick: () -> Unit,
    override val dataId: Long = label.hashCode().toLong()
) : ListModel, ComposableModel {
    override val layoutId = R.layout.composable_list_item

    @Composable
    override fun Content(modifier: Modifier) {
        LabelRatingListItem(
            model = this,
            modifier = modifier
        )
    }
}
