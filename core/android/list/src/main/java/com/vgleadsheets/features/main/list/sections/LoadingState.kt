package com.vgleadsheets.features.main.list.sections

import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingImageNameCaptionListModel
import com.vgleadsheets.components.LoadingNameCaptionListModel
import com.vgleadsheets.features.main.list.LoadingItemStyle

object LoadingState {
    fun listItems(
        isDataLoading: Boolean,
        loadingItemStyle: LoadingItemStyle,
        loadingGenerator: (() -> List<ListModel>)? = null,
    ) = if (!isDataLoading) {
        emptyList()
    } else if (loadingGenerator != null) {
        loadingGenerator()
    } else {
        buildList {
            repeat(LOADING_ITEMS) {
                add(
                    when (loadingItemStyle) {
                        LoadingItemStyle.REGULAR -> LoadingNameCaptionListModel(
                            "allData",
                            it
                        )
                        LoadingItemStyle.WITH_IMAGE -> LoadingImageNameCaptionListModel(
                            "allData",
                            it
                        )
                    }
                )
            }
        }
    }

    private const val LOADING_ITEMS = 15

    data class Config(
        val isDataLoading: Boolean,
        val loadingItemStyle: LoadingItemStyle,
        val loadingGenerator: (() -> List<ListModel>)? = null,
    )
}
