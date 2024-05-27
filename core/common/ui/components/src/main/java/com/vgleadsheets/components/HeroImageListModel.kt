package com.vgleadsheets.components

import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.ui.Icon

data class HeroImageListModel(
    val imageUrl: String,
    val imagePlaceholder: Icon,
    val name: String? = null,
    val caption: String? = null,
    val clickAction: VglsAction
) : ListModel() {
    override val dataId = imageUrl.hashCode().toLong()
    override val columns = ListModel.COLUMNS_ALL
}