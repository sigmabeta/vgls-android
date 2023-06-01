package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.composables.LabelValueListItem

data class LabelValueListModel(
    val label: String,
    val value: String,
    val onClick: () -> Unit,
    override val dataId: Long = label.hashCode().toLong()
) : ListModel {
    override val layoutId = R.layout.list_component_label_value

    @Composable
    override fun Content(modifier: Modifier) {
        LabelValueListItem(
            model = this,
            modifier = modifier
        )
    }
}
