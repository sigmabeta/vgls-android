package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import com.vgleadsheets.composables.LabelValueListItem

data class LabelValueListModel(
    val label: String,
    val value: String,
    val onClick: () -> Unit,
    override val dataId: Long = label.hashCode().toLong()
) : ListModel, ComposableModel {
    override val layoutId = R.layout.composable_menu_item

    @Composable
    override fun Content() {
        LabelValueListItem(model = this)
    }
}
