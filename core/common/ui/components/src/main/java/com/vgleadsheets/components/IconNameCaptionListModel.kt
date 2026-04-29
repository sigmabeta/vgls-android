package com.vgleadsheets.components

import com.vgleadsheets.appcomm.VglsAction
import net.sigmabeta.sage.ui.Icon

data class IconNameCaptionListModel(
    override val dataId: Long,
    val name: String,
    val caption: String,
    val icon: Icon,
    val clickAction: VglsAction
) : ListModel() {
    override val columns = ListModel.COLUMNS_ALL
}
