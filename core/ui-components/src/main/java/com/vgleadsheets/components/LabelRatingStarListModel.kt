package com.vgleadsheets.components

data class LabelRatingStarListModel(
    val label: String,
    val value: Int,
    val onClick: () -> Unit,
    override val dataId: Long = label.hashCode().toLong()
) : ListModel {
    override val layoutId = R.layout.list_component_label_rating
}
