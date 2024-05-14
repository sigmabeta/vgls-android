package com.vgleadsheets.components

import kotlinx.collections.immutable.ImmutableList

data class HorizontalScrollerListModel(
    override val dataId: Long,
    val scrollingItems: ImmutableList<ListModel>,
) : ListModel() {
    override val columns = 1
}
