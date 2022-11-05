package com.vgleadsheets.images

import android.net.Uri

object Page {
    fun generateImageUrl(
        baseImageUrl: String,
        partApiId: String,
        filename: String,
        pageNumber: Int
    ): String {
        return baseImageUrl +
            partApiId + URL_SEPARATOR_FOLDER +
            Uri.encode(filename) + URL_SEPARATOR_NUMBER +
            pageNumber + URL_FILE_EXT_PNG
    }

    fun generateThumbUrl(
        baseImageUrl: String,
        selectedPartApiId: String,
        filename: String
    ) = generateImageUrl(
        baseImageUrl,
        selectedPartApiId,
        filename,
        1
    )

    private const val URL_SEPARATOR_FOLDER = "/"
    private const val URL_SEPARATOR_NUMBER = "-"
    private const val URL_FILE_EXT_PNG = ".png"
}

