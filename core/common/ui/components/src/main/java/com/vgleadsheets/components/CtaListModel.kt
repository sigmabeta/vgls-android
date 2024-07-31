package com.vgleadsheets.components

import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.ui.Icon

data class CtaListModel(
    val icon: Icon,
    val name: String,
    val clickAction: VglsAction,
    override val dataId: Long = name.hashCode().toLong(),
) : ListModel() {
    override val columns = ListModel.COLUMNS_ALL
}
