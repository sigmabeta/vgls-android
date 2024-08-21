package com.vgleadsheets.remaster.home

import com.vgleadsheets.components.HorizontalScrollerListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingItemListModel
import com.vgleadsheets.components.LoadingSectionHeaderListModel
import com.vgleadsheets.components.LoadingType
import com.vgleadsheets.components.SectionHeaderListModel
import kotlinx.collections.immutable.toImmutableList

data class HomeModuleState(
    val moduleName: String,
    val shouldShow: Boolean = false,
    val isLoading: Boolean = false,
    val loadingType: LoadingType = LoadingType.SQUARE,
    val title: String? = null,
    val items: List<ListModel> = emptyList()
) {
    fun toListItems() = if (shouldShow) {
        if (isLoading) {
            createTitleLoadingListModel(title) + createHorizScrollerLoadingListModel()
        } else {
            createTitleListModel(title) + createHorizScrollerListModel()
        }
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

    private fun createTitleLoadingListModel(title: String?) = if (title != null) {
        listOf(
            LoadingSectionHeaderListModel(
                loadOperationName = "$title.loading.header",
                loadPositionOffset = 0
            )
        )
    } else {
        emptyList()
    }

    @Suppress("MagicNumber")
    private fun createHorizScrollerLoadingListModel(): List<HorizontalScrollerListModel> {
        val items = List(5) { index ->
            LoadingItemListModel(
                loadingType = loadingType,
                loadOperationName = moduleName,
                loadPositionOffset = index
            )
        }

        val dataId = (title.hashCode() + items.hashCode()).toLong()
        return listOf(
            HorizontalScrollerListModel(
                dataId = dataId,
                scrollingItems = items.toImmutableList()
            )
        )
    }
}
