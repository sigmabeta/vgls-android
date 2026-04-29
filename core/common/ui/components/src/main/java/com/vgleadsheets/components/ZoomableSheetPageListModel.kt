package com.vgleadsheets.components

import net.sigmabeta.sage.appcomm.VglsAction
import com.vgleadsheets.pdf.PdfConfigById
import kotlinx.collections.immutable.ImmutableList

data class ZoomableSheetPageListModel(
    val pdfConfigById: PdfConfigById,
    val title: String,
    val gameName: String,
    val composers: ImmutableList<String>,
    val pageNumber: Int,
    val clickAction: VglsAction,
    override val dataId: Long = ("$gameName - $title: Page $pageNumber").hashCode().toLong()
) : ListModel() {
    override val columns = COLUMNS_ALL
}
