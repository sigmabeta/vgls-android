package com.vgleadsheets.pdf

import net.sigmabeta.sage.pdf.PdfConfigById

fun PdfConfigById.cacheKey(
    width: Int?,
    height: Int?,
    partApiId: String
): String = "pdf-${keyCommonSection(partApiId, width, height)}"

fun PdfConfigById.fakeCacheKey(
    width: Int?,
    height: Int?,
    partApiId: String
): String = "fakepdf-${keyCommonSection(partApiId, width, height)}"

private fun PdfConfigById.keyCommonSection(
    partApiId: String,
    width: Int?,
    height: Int?
): String = "$songId-$partApiId-alt=$isAltSelected-$pageNumber-$width-$height"

const val ZOOM_MAX_PDF = 4
