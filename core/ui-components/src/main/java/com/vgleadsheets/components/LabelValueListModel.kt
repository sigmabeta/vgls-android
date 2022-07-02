package com.vgleadsheets.components

data class LabelValueListModel(
    val label: String,
    val value: String,
    val onClick: () -> Unit,
    override val dataId: Long = label.hashCode().toLong()
) : ListModel {
    override val layoutId = R.layout.list_component_label_value
}
