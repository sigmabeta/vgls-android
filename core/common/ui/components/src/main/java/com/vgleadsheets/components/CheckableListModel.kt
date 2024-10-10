package com.vgleadsheets.components

import com.vgleadsheets.appcomm.VglsAction

data class CheckableListModel(
    val settingId: String,
    val name: String,
    val checked: Boolean?,
    val clickAction: VglsAction,
) : ListModel() {
    override val dataId: Long = settingId.hashCode().toLong()
    override val columns = ListModel.COLUMNS_ALL
}
