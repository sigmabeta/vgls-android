package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.composables.MenuTitleBar
import com.vgleadsheets.ui.components.R

data class MenuTitleBarListModel(
    val partLabel: String,
    val iconId: Int,
    val onSearchButtonClick: () -> Unit,
    val onMenuButtonClick: () -> Unit,
    val onChangePartClick: () -> Unit
) : ListModel {
    override val dataId = javaClass.simpleName.hashCode().toLong()
    override val layoutId = R.layout.list_component_menu_title_bar
    override val columns = ListModel.COLUMNS_ALL

    @Composable
    override fun Content(modifier: Modifier) {
        MenuTitleBar(
            model = this,
            modifier = modifier
        )
    }
}
