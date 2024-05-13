package com.vgleadsheets.components

data class DropdownSettingListModel(
    val settingId: String,
    val name: String,
    val selectedPosition: Int,
    val settingsLabels: List<String>,
    val onNewOptionSelected: (Int) -> Unit,
) : ListModel() {
    override val dataId: Long = settingId.hashCode().toLong()
    override val columns = ListModel.COLUMNS_ALL
}
