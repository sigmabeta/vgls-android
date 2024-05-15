package com.vgleadsheets.components

import com.vgleadsheets.state.VglsAction

data class SingleTextListModel(
    override val dataId: Long,
    val name: String,
    val clickAction: VglsAction,
) : ListModel() {
    override val columns = ListModel.COLUMNS_ALL
}
