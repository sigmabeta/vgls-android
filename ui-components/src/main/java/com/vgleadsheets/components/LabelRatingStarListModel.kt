package com.vgleadsheets.components

data class LabelRatingStarListModel(
    val label: String,
    val value: Int,
    val handler: EventHandler,
    override val dataId: Long = label.hashCode().toLong()
) : ListModel {
    interface EventHandler {
        fun onClicked(clicked: LabelRatingStarListModel)
        fun clearClicked()
    }

    override val layoutId = R.layout.list_component_label_rating
}
