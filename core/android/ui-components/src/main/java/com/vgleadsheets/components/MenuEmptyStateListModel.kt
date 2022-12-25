package com.vgleadsheets.components

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import com.vgleadsheets.composables.EmptyListIndicator

data class MenuEmptyStateListModel(
    @DrawableRes
    val iconId: Int,
    val explanation: String,
    val showCrossOut: Boolean = true
) : ListModel, ComposableModel {
    override val dataId = explanation.hashCode().toLong()
    override val layoutId = R.layout.composable_menu_item

    @Composable
    override fun Content() {
        EmptyListIndicator(
            model = this
        )
    }
}
