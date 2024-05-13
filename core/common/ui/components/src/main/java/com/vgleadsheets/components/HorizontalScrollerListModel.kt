package com.vgleadsheets.components


data class HorizontalScrollerListModel(
    override val dataId: Long,
    val scrollingItems: List<ListModel>,
) : ListModel() {
    override val columns = 1
}
