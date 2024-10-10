package com.vgleadsheets.images

object Page {
    fun generateImageUrl(
        baseImageUrl: String,
        partApiId: String,
        filename: String,
        isAlternateEnabled: Boolean,
        pageNumber: Int
    ): String {
        return baseImageUrl +
            partApiId + URL_SEPARATOR_FOLDER +
            filename.replace(" ", "%20") +
            (if (isAlternateEnabled) IDENTIFIER_ALT else "") +
            URL_SEPARATOR_NUMBER +
            pageNumber + URL_FILE_EXT_PNG
    }

    fun generateThumbUrl(
        baseImageUrl: String,
        selectedPartApiId: String,
        isAlternateEnabled: Boolean,
        filename: String
    ) = generateImageUrl(
        baseImageUrl,
        selectedPartApiId,
        filename,
        isAlternateEnabled,
        1
    )

    private const val IDENTIFIER_ALT = " [ALT]"

    private const val URL_SEPARATOR_FOLDER = "/"
    private const val URL_SEPARATOR_NUMBER = "-"
    private const val URL_FILE_EXT_PNG = ".png"
}
