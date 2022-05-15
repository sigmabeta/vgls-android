package com.vgleadsheets.components

data class LabelValueListModel(
    val label: String,
    val value: String,
    val handler: EventHandler,
    override val dataId: Long = label.hashCode().toLong()
) : ListModel {
    interface EventHandler {
        fun onClicked(clicked: LabelValueListModel)
        fun clearClicked()
    }

    override val layoutId = R.layout.list_component_label_value
}
