package com.vgleadsheets.components

data class PartListModel(
    override val dataId: Long,
    val name: String,
    val selected: Boolean,
    val listener: ClickListener,
    override val layoutId: Int = R.layout.list_component_part
) : ListModel {
    interface ClickListener {
        fun onClicked(clicked: PartListModel)
    }
}
