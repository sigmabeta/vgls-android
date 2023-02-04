package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.composables.LabelCheckboxItem

data class CheckableListModel(
    val settingId: String,
    val name: String,
    val checked: Boolean,
    val onClick: () -> Unit,
) : ListModel, ComposableModel {
    override val dataId: Long = settingId.hashCode().toLong()
    override val layoutId = R.layout.composable_list_item

    @Composable
    override fun Content(modifier: Modifier) {
        LabelCheckboxItem(
            model = this,
            modifier = modifier
        )
    }
}
