package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.composables.MenuItem
import com.vgleadsheets.ui.components.R

data class MenuItemListModel(
    val name: String,
    val caption: String?,
    val iconId: Int,
    val onClick: () -> Unit,
    val selected: Boolean = false
) : ListModel {
    override val dataId = name.hashCode().toLong()
    override val layoutId = R.layout.list_component_menu_item
    override val columns = ListModel.COLUMNS_ALL

    @Composable
    override fun Content(modifier: Modifier) {
        MenuItem(
            model = this,
            modifier = modifier
        )
    }
}
