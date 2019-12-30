package com.vgleadsheets.components

data class CtaListModel(
    val iconId: Int,
    val name: String,
    val handler: EventHandler
) : ListModel {
    interface EventHandler {
        fun onClicked(clicked: CtaListModel)
    }

    override val dataId = iconId.toLong()

    override val layoutId = R.layout.list_component_cta
}
