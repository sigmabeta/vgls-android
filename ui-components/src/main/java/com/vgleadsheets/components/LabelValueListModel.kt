package com.vgleadsheets.components

data class LabelValueListModel(
    val label: String,
    val value: String,
    val screenName: String,
    val handler: EventHandler,
    override val dataId: Long = label.hashCode().toLong()
) : ListModel {
    interface EventHandler {
        fun onClicked(clicked: LabelValueListModel)
        fun clearClicked()
        fun onLabelValueLoaded(screenName: String)
    }

    override val layoutId = R.layout.list_component_label_value
}
