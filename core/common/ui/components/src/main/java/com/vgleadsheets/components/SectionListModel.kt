package com.vgleadsheets.components

import kotlinx.collections.immutable.ImmutableList

data class SectionListModel(
    override val dataId: Long,
    override val columns: Int = 1,
    val dontUnroll: Boolean = false,
    val sectionItems: ImmutableList<ListModel>,
) : ListModel() {
}
