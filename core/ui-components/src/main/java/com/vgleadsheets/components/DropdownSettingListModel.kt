package com.vgleadsheets.components

data class DropdownSettingListModel(
    val settingId: String,
    val name: String,
    val selectedPosition: Int,
    val settingsLabels: List<String>,
    val handler: EventHandler
) : ListModel {
    interface EventHandler {
        fun onNewOptionSelected(settingId: String, selectedPosition: Int)
    }

    override val dataId: Long = settingId.hashCode().toLong()
    override val layoutId = R.layout.list_component_dropdown
}
