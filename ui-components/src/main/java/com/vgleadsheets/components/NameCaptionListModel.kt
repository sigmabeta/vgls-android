package com.vgleadsheets.components

import com.vgleadsheets.PerfHandler

data class NameCaptionListModel(
    override val dataId: Long,
    val name: String,
    val caption: String,
    val handler: EventHandler,
    val perfHandler: PerfHandler
) : ListModel {
    override val layoutId = R.layout.list_component_name_caption

    interface EventHandler {
        fun onClicked(clicked: NameCaptionListModel)
        fun clearClicked()
    }
}
