package com.vgleadsheets.pdf

import coil3.request.Options
import coil3.size.Size
import coil3.size.pxOrElse

internal fun computeWidth(options: Options): Int? {
    val size = options.size
    return if (size == Size.ORIGINAL) {
        null
    } else {
        size.width.pxOrElse { WIDTH_ARBITRARY }
    }
}

internal fun computeHeight(options: Options): Int? {
    val size = options.size
    return if (size == Size.ORIGINAL) {
        null
    } else {
        size.height.pxOrElse { HEIGHT_ARBITRARY }
    }
}

fun PdfConfigById.cacheKey(width: Int?, height: Int?, partApiId: String): String {
    return "pdf-${keyCommonSection(partApiId, width, height)}"
}

fun PdfConfigById.fakeCacheKey(width: Int?, height: Int?,partApiId: String): String {
    return "fakepdf-${keyCommonSection(partApiId, width, height)}"
}

private fun PdfConfigById.keyCommonSection(
    partApiId: String,
    width: Int?,
    height: Int?
): String = "$songId-$partApiId-alt=$isAltSelected-$pageNumber-$width-$height"

private const val WIDTH_ARBITRARY = 69
private const val HEIGHT_ARBITRARY = 69
