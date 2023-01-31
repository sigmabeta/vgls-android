package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.composables.MenuItem

data class MenuItemListModel(
    val name: String,
    val caption: String?,
    val iconId: Int,
    val onClick: () -> Unit,
    val selected: Boolean = false
) : ListModel, ComposableModel {
    override val dataId = name.hashCode().toLong()
    override val layoutId = R.layout.list_component_menu_item

    @Composable
    override fun Content(modifier: Modifier) {
        MenuItem(
            model = this,
            modifier = modifier
        )
    }
}
