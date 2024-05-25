package com.vgleadsheets.components

import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.ui.Icon

data class SearchResultListModel(
    override val dataId: Long,
    val name: String,
    val caption: String,
    val imageUrl: String?,
    val imagePlaceholder: Icon,
    val actionableId: Long? = null,
    val clickAction: VglsAction
) : ListModel() {
    override val columns = ListModel.COLUMNS_ALL
}
