package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import com.vgleadsheets.composables.MoreResultsLink

data class MenuLoadingItemListModel(
    val name: String,
    val iconId: Int
) : ListModel, ComposableModel {
    override val dataId = iconId.toLong()

    override val layoutId = R.layout.composable_menu_item

    @Composable
    override fun Content() {
        MoreResultsLink(
            model = MenuSearchMoreListModel(
                name
            ) {}
        )
    }
}
