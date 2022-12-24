package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import com.vgleadsheets.composables.MenuSearchBar

data class MenuSearchListModel(
    val searchQuery: String?,
    val onTextEntered: (String) -> Unit,
    val onMenuButtonClick: () -> Unit,
    val onClearClick: () -> Unit
) : ListModel, ComposableModel {

    override val dataId = javaClass.simpleName.hashCode().toLong()

    override val layoutId = R.layout.composable_menu_item

    @Composable
    override fun Content() {
        MenuSearchBar(model = this)
    }
}
