package com.vgleadsheets.remaster.home

import com.vgleadsheets.components.HorizontalScrollerListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.SectionHeaderListModel
import kotlinx.collections.immutable.toImmutableList

data class HomeModuleState(
    val shouldShow: Boolean = false,
    val priority: Priority = Priority.MID,
    val title: String? = null,
    val items: List<ListModel> = emptyList()
) {
    fun toListItems() = if (shouldShow) {
        createTitleListModel(title) + createHorizScrollerListModel()
    } else {
        emptyList()
    }

    private fun createTitleListModel(title: String?) = if (title != null) {
        listOf(SectionHeaderListModel(title))
    } else {
        emptyList()
    }

    private fun createHorizScrollerListModel(): List<HorizontalScrollerListModel> {
        val dataId = (title.hashCode() + items.hashCode()).toLong()
        return listOf(
            HorizontalScrollerListModel(
                dataId = dataId,
                scrollingItems = items.toImmutableList()
            )
        )
    }
}
