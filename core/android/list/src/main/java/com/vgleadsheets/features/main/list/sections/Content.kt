package com.vgleadsheets.features.main.list.sections

import com.vgleadsheets.components.ListModel

object Content {
    suspend fun listItems(
        isDataReady: Boolean,
        dataGenerator: suspend () -> List<ListModel>
    ) = if (!isDataReady) {
        emptyList()
    } else {
        dataGenerator()
    }

    data class Config(
        val isDataReady: Boolean,
        val dataGenerator: suspend () -> List<ListModel>
    )
}
