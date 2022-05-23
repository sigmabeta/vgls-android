package com.vgleadsheets.features.main.list.sections

import com.vgleadsheets.components.ListModel

object Content {
    fun listItems(
        isDataReady: Boolean,
        dataGenerator: (() -> List<ListModel>)
    ) = if (!isDataReady) {
        emptyList()
    } else {
        dataGenerator()
    }

    data class Config(
        val isDataReady: Boolean,
        val dataGenerator: (() -> List<ListModel>)
    )
}
