package com.vgleadsheets.components

import net.sigmabeta.sage.appcomm.VglsAction
import net.sigmabeta.sage.ui.Icon

data class WideItemListModel(
    override val dataId: Long,
    val name: String,
    val sourceInfo: String?,
    val imagePlaceholder: Icon,
    val actionableId: Long? = null,
    val clickAction: VglsAction
) : ListModel() {
    override val columns = 1
}
