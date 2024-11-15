package com.vgleadsheets.components

import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.images.SourceInfo
import kotlinx.collections.immutable.ImmutableList

data class ZoomableSheetPageListModel(
    val sourceInfo: SourceInfo,
    val title: String,
    val gameName: String,
    val composers: ImmutableList<String>,
    val pageNumber: Int,
    val actuallyZoomable: Boolean,
    val clickAction: VglsAction,
    override val dataId: Long = ("$gameName - $title: Page $pageNumber").hashCode().toLong()
) : ListModel() {
    override val columns = COLUMNS_ALL
}
