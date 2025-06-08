package com.vgleadsheets.pdf

fun PdfConfigById.cacheKey(width: Int?, height: Int?, partApiId: String): String {
    return "pdf-${keyCommonSection(partApiId, width, height)}"
}

fun PdfConfigById.fakeCacheKey(width: Int?, height: Int?, partApiId: String): String {
    return "fakepdf-${keyCommonSection(partApiId, width, height)}"
}

private fun PdfConfigById.keyCommonSection(
    partApiId: String,
    width: Int?,
    height: Int?
): String = "$songId-$partApiId-alt=$isAltSelected-$pageNumber-$width-$height"

const val ZOOM_MAX_PDF = 8
