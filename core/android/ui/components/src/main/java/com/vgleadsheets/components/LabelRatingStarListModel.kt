package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.composables.LabelRatingListItem
import com.vgleadsheets.ui.components.R

data class LabelRatingStarListModel(
    val label: String,
    val value: Int,
    val onClick: () -> Unit,
    override val dataId: Long = label.hashCode().toLong()
) : ListModel {
    override val layoutId = R.layout.list_component_label_rating

    @Composable
    override fun Content(modifier: Modifier) {
        LabelRatingListItem(
            model = this,
            modifier = modifier
        )
    }
}
