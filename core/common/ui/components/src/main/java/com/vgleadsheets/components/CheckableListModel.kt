package com.vgleadsheets.components

import net.sigmabeta.sage.appcomm.VglsAction

data class CheckableListModel(
    val settingId: String,
    val name: String,
    val checked: Boolean?,
    val clickAction: VglsAction,
) : ListModel() {
    override val dataId: Long = settingId.hashCode().toLong()
    override val columns = ListModel.COLUMNS_ALL
}
