package com.vgleadsheets.components

import kotlinx.collections.immutable.ImmutableList

data class SectionListModel(
    override val dataId: Long,
    override val columns: Int = 1,
    val sectionItems: ImmutableList<ListModel>,
) : ListModel() {
}
