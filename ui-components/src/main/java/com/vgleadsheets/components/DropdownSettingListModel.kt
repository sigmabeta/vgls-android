package com.vgleadsheets.components

data class DropdownSettingListModel(
    val settingId: String,
    val name: String,
    val selectedPosition: Int,
    val settingsLabels: List<String>,
    val settingsValues: List<String>,
    val handler: EventHandler
): ListModel {
    interface EventHandler {
        fun onNewOptionSelected(settingId: String, value: String)
    }

    init {
        if (settingsLabels.size != settingsValues.size) {
            throw IllegalArgumentException("Not enough labels or values!")
        }
    }

    override val dataId: Long = settingId.hashCode().toLong()
    override val layoutId = R.layout.list_component_dropdown
}
