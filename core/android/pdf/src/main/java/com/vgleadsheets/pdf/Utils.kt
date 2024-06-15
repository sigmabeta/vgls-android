package com.vgleadsheets.pdf

import coil.request.Options
import coil.size.Size
import coil.size.pxOrElse

internal fun computeWidth(options: Options): Int? {
    val size = options.size
    return if (size == Size.ORIGINAL) {
        null
    } else {
        size.width.pxOrElse { WIDTH_ARBITRARY }
    }
}

fun PdfConfigById.cacheKey(width: Int?, partApiId: String): String {
    return "pdf-$songId-$partApiId-$pageNumber-$width"
}

private const val WIDTH_ARBITRARY = 69
