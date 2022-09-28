package com.vgleadsheets.components

import androidx.annotation.DrawableRes

data class MenuEmptyStateListModel(
    @DrawableRes
    val iconId: Int,
    val explanation: String,
    val showCrossOut: Boolean = true
) :
    ListModel {
    override val dataId = explanation.hashCode().toLong()
    override val layoutId = R.layout.list_component_menu_empty_state
}
