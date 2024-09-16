package com.vgleadsheets.remaster.home

import com.vgleadsheets.components.HorizontalScrollerListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingItemListModel
import com.vgleadsheets.components.LoadingType
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.list.ifNotNull
import kotlinx.collections.immutable.toImmutableList

data class HomeModuleState(
    val moduleName: String,
    val shouldShow: Boolean = false,
    val isLoading: Boolean = false,
    val loadingType: LoadingType = LoadingType.SQUARE,
    val title: String? = null,
    val items: List<ListModel> = emptyList()
) {
    fun toListItems(): List<ListModel> = if (shouldShow) {
        if (isLoading) {
            listOf(
                createTitleLoadingListModel(title),
                createHorizScrollerLoadingListModel()
            )
        } else {
            listOf(
                createTitleListModel(title),
                createHorizScrollerListModel()
            )
        }
    } else {
        emptyList()
    }

    private fun createTitleListModel(title: String?) = ifNotNull(title) {
        SectionHeaderListModel(it)
    }

    private fun createHorizScrollerListModel(): HorizontalScrollerListModel {
        val dataId = ("$moduleName.items".hashCode()).toLong()

        return HorizontalScrollerListModel(
            dataId = dataId,
            scrollingItems = items.toImmutableList()
        )
    }

    private fun createTitleLoadingListModel(title: String?) = ifNotNull(title) {
        LoadingItemListModel(
            loadOperationName = "$title.loading.header",
            loadingType = LoadingType.SECTION_HEADER,
            loadPositionOffset = 0
        )
    }

    @Suppress("MagicNumber")
    private fun createHorizScrollerLoadingListModel(): HorizontalScrollerListModel {
        val items = List(5) { index ->
            LoadingItemListModel(
                loadingType = loadingType,
                loadOperationName = moduleName,
                loadPositionOffset = index
            )
        }

        val dataId = ("$moduleName.items".hashCode()).toLong()
        return HorizontalScrollerListModel(
            dataId = dataId,
            scrollingItems = items.toImmutableList()
        )
    }
}
