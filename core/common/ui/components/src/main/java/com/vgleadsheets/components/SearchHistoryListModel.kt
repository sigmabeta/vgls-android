package com.vgleadsheets.components

import net.sigmabeta.sage.appcomm.VglsAction

data class SearchHistoryListModel(
    override val dataId: Long,
    val name: String,
    val clickAction: VglsAction,
    val removeAction: VglsAction,
) : ListModel() {
    override val columns = ListModel.COLUMNS_ALL
}
