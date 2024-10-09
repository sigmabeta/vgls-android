package com.vgleadsheets.components

import kotlinx.collections.immutable.ImmutableList

data class CollapsibleDetailsListModel(
    override val dataId: Long,
    val title: String,
    val detailItems: ImmutableList<String>,
    val initiallyCollapsed: Boolean = false,
) : ListModel() {
    override val columns = COLUMNS_ALL
}
