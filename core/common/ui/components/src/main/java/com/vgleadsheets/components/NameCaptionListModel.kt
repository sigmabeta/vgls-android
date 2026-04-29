package com.vgleadsheets.components

import net.sigmabeta.sage.appcomm.VglsAction

data class NameCaptionListModel(
    override val dataId: Long,
    val name: String,
    val caption: String,
    val clickAction: VglsAction,
) : ListModel() {
    override val columns = ListModel.COLUMNS_ALL
}
