package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.composables.ActionItem
import com.vgleadsheets.ui.components.R

data class CtaListModel(
    val iconId: Int,
    val name: String,
    val onClick: () -> Unit,
) : ListModel {
    override val dataId = name.hashCode().toLong()
    override val layoutId = R.layout.list_component_cta
    override val columns = ListModel.COLUMNS_ALL

    @Composable
    override fun Content(modifier: Modifier) {
        ActionItem(
            model = this,
            modifier = modifier
        )
    }
}
