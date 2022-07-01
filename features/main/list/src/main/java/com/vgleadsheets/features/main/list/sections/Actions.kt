package com.vgleadsheets.features.main.list.sections

import com.vgleadsheets.components.ListModel

object Actions {
    fun listItems(
        shouldShow: Boolean,
        actionList: List<ListModel>
    ) = if (!shouldShow) {
        emptyList()
    } else {
        actionList
    }

    data class Config(
        val shouldShow: Boolean,
        val actionList: List<ListModel>
    )

    val NONE = Config(
        false,
        emptyList()
    )
}
