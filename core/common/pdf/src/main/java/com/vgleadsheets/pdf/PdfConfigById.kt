package com.vgleadsheets.pdf

data class PdfConfigById(
    val songId: Long,
    val partApiId: String,
    val pageNumber: Int,
)
