package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.composables.LabelCheckboxItem
import com.vgleadsheets.ui.components.R

data class CheckableListModel(
    val settingId: String,
    val name: String,
    val checked: Boolean,
    val onClick: () -> Unit,
) : ListModel {
    override val dataId: Long = settingId.hashCode().toLong()
    override val layoutId = R.layout.list_component_checkable
    override val columns = ListModel.COLUMNS_ALL

    @Composable
    override fun Content(modifier: Modifier) {
        LabelCheckboxItem(
            model = this,
            modifier = modifier
        )
    }
}
