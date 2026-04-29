package com.vgleadsheets.components

import net.sigmabeta.sage.appcomm.VglsAction

data class SingleTextListModel(
    val name: String,
    val clickAction: VglsAction,
    override val dataId: Long = name.hashCode().toLong(),
) : ListModel() {
    override val columns = ListModel.COLUMNS_ALL
}
