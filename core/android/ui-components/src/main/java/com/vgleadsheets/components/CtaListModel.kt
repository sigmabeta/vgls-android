package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.composables.ActionItem

data class CtaListModel(
    val iconId: Int,
    val name: String,
    val onClick: () -> Unit,
) : ListModel {
    override val dataId = name.hashCode().toLong()
    override val layoutId = R.layout.list_component_cta

    @Composable
    override fun Content(modifier: Modifier) {
        ActionItem(
            model = this,
            modifier = modifier
        )
    }
}
