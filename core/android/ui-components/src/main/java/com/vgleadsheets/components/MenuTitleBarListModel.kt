package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import com.vgleadsheets.composables.MenuTitleBar

data class MenuTitleBarListModel(
    val partLabel: String,
    val iconId: Int,
    val onSearchButtonClick: () -> Unit,
    val onMenuButtonClick: () -> Unit,
    val onChangePartClick: () -> Unit
) : ListModel, ComposableModel {

    override val dataId = javaClass.simpleName.hashCode().toLong()

    override val layoutId = R.layout.list_component_menu_title_bar

    @Composable
    override fun Content() {
        MenuTitleBar(model = this)
    }
}
