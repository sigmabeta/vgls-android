package com.vgleadsheets.components

data class PartListModel(
    override val dataId: Long,
    val name: String,
    val selected: Boolean,
    val listener: ClickListener
) : ListModel {
    override val layoutId = R.layout.list_component_part

    interface ClickListener {
        fun onClicked(clicked: PartListModel)
    }
}
