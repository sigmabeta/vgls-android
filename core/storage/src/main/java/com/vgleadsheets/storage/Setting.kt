package com.vgleadsheets.storage

sealed class Setting {
    abstract val settingId: String
    abstract val labelStringId: Int
}

data class BooleanSetting(
    override val settingId: String,
    override val labelStringId: Int,
    val value: Boolean
) : Setting()

data class DropdownSetting(
    override val settingId: String,
    override val labelStringId: Int,
    val selectedPosition: Int,
    val valueStringIds: List<Int>
) : Setting()
