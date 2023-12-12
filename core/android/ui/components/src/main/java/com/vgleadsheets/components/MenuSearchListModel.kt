package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.composables.MenuSearchBar
import com.vgleadsheets.ui.components.R

data class MenuSearchListModel(
    val searchQuery: String?,
    val onTextEntered: (String) -> Unit,
    val onMenuButtonClick: () -> Unit,
    val onClearClick: () -> Unit
) : ListModel {

    override val dataId = javaClass.simpleName.hashCode().toLong()

    override val layoutId = R.layout.list_component_menu_search_bar

    @Composable
    override fun Content(modifier: Modifier) {
        MenuSearchBar(
            model = this,
            modifier = modifier
        )
    }
}
