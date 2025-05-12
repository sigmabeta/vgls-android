package com.vgleadsheets.pdf

data class PdfConfigById(
    val songId: Long,
    val pageNumber: Int,
    val isAltSelected: Boolean,
    val pdfSize: PdfSize,
    val maxWidth: Int? = null,
    val maxHeight: Int? = null,
)
