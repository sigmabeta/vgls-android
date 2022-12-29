package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import com.vgleadsheets.composables.MoreResultsLink

data class MenuSearchMoreListModel(
    val text: String,
    val onClick: () -> Unit,
) : ListModel, ComposableModel {
    override val dataId = text.hashCode().toLong()

    override val layoutId = R.layout.list_component_menu_search_more

    @Composable
    override fun Content() {
        MoreResultsLink(model = this)
    }
}
