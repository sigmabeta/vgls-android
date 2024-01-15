package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.composables.MoreResultsLink
import com.vgleadsheets.ui.components.R

data class MenuLoadingItemListModel(
    val name: String,
    val iconId: Int
) : ListModel {
    override val dataId = iconId.toLong()
    override val layoutId = R.layout.list_component_menu_loading
    override val columns = ListModel.COLUMNS_ALL

    @Composable
    override fun Content(modifier: Modifier) {
        MoreResultsLink(
            modifier = modifier,
            model = MenuSearchMoreListModel(
                name
            ) {}
        )
    }
}
