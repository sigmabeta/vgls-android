package com.vgleadsheets.components

import com.vgleadsheets.appcomm.VglsAction

data class PdfTestListModel(
    val songId: Long,
    val partApiId: String,
    val pageNumber: Int,
    val clickAction: VglsAction,
) : ListModel() {
    override val dataId: Long = songId
    override val columns = ListModel.COLUMNS_ALL
}
