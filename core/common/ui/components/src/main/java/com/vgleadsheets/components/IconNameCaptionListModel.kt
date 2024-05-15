package com.vgleadsheets.components

import com.vgleadsheets.state.VglsAction

data class IconNameCaptionListModel(
    override val dataId: Long,
    val name: String,
    val caption: String,
    val iconId: Int,
    val clickAction: VglsAction
) : ListModel() {
    override val columns = ListModel.COLUMNS_ALL
}
