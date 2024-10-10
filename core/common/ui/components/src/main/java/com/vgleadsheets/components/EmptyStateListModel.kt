package com.vgleadsheets.components

import com.vgleadsheets.ui.Icon

data class EmptyStateListModel(
    val icon: Icon,
    val explanation: String,
    val debugText: String? = null,
    val showCrossOut: Boolean = true
) : ListModel() {
    override val dataId = explanation.hashCode().toLong()
    override val columns = ListModel.COLUMNS_ALL
}
