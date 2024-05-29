package com.vgleadsheets.components

import com.vgleadsheets.appcomm.VglsAction

data class PdfTestListModel(
    val filename: String,
    val pageNumber: Int,
    val clickAction: VglsAction,
) : ListModel() {
    override val dataId: Long = filename.hashCode().toLong()
    override val columns = ListModel.COLUMNS_ALL
}
