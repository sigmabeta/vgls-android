package com.vgleadsheets.components

import kotlinx.collections.immutable.ImmutableList

data class DropdownSettingListModel(
    val settingId: String,
    val name: String,
    val selectedPosition: Int,
    val settingsLabels: ImmutableList<String>,
    val onNewOptionSelected: (Int) -> Unit,
) : ListModel() {
    override val dataId: Long = settingId.hashCode().toLong()
    override val columns = ListModel.COLUMNS_ALL
}
