package com.vgleadsheets.components

import kotlinx.collections.immutable.ImmutableList

data class SectionListModel(
    override val dataId: Long,
    val sectionItems: ImmutableList<ListModel>,
) : ListModel() {
    override val columns = 1
}
