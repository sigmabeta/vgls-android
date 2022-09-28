package com.vgleadsheets.components

data class CheckableListModel(
    val settingId: String,
    val name: String,
    val checked: Boolean,
    val onClick: () -> Unit,
) : ListModel {
    override val dataId: Long = settingId.hashCode().toLong()
    override val layoutId = R.layout.list_component_checkable
}
