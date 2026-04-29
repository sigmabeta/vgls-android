package com.vgleadsheets.components

import net.sigmabeta.sage.appcomm.VglsAction

data class NotifListModel(
    override val dataId: Long,
    val title: String,
    val description: String,
    val actionLabel: String,
    val action: VglsAction?,
    val isError: Boolean
) : ListModel() {
    override val columns = ListModel.COLUMNS_ALL
}
