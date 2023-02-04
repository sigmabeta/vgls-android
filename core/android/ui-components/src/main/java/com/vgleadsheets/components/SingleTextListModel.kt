package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.composables.LabelNoThingyItem

data class SingleTextListModel(
    override val dataId: Long,
    val name: String,
    val onClick: () -> Unit,
) : ListModel, ComposableModel {
    override val layoutId = R.layout.composable_list_item

    @Composable
    override fun Content(modifier: Modifier) {
        LabelNoThingyItem(
            model = this,
            modifier = modifier
        )
    }
}
