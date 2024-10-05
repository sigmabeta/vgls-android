package com.vgleadsheets.components

import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.images.SourceInfo
import com.vgleadsheets.ui.Icon

data class HeroImageListModel(
    val sourceInfo: SourceInfo,
    val imagePlaceholder: Icon,
    val clickAction: VglsAction
) : ListModel() {
    override val dataId = sourceInfo.hashCode().toLong()
    override val columns = 1
}
