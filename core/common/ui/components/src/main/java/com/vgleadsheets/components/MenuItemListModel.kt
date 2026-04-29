package com.vgleadsheets.components

import com.vgleadsheets.appcomm.VglsAction
import net.sigmabeta.sage.ui.Icon

data class MenuItemListModel(
    val name: String,
    val caption: String?,
    val icon: Icon,
    val clickAction: VglsAction,
    val selected: Boolean = false
) : ListModel() {
    override val dataId = name.hashCode().toLong()
    override val columns = COLUMNS_ALL
}
