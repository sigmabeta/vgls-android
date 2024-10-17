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

fun PdfConfigById.cacheKey(width: Int?, partApiId: String): String {
    return "pdf-$songId-$partApiId-alt=$isAltSelected-$pageNumber-$width"
}

fun PdfConfigById.fakeCacheKey(width: Int?, partApiId: String): String {
    return "fakepdf-$songId-$partApiId-alt=$isAltSelected-$pageNumber-$width"
}

private const val WIDTH_ARBITRARY = 69
