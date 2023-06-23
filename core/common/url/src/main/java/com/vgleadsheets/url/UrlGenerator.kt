package com.vgleadsheets.url


object UrlGenerator {
    fun generateSongPageImageUrl(
        baseImageUrl: String,
        partApiId: String,
        filename: String,
        isAlternateEnabled: Boolean,
        pageNumber: Int
    ) = baseImageUrl +
        partApiId +
        URL_SEPARATOR_FOLDER +
        filename +
        (if (isAlternateEnabled) IDENTIFIER_ALT else "") +
        URL_SEPARATOR_NUMBER +
        pageNumber + URL_FILE_EXT_PNG

    private const val IDENTIFIER_ALT = " [ALT]"

    private const val URL_SEPARATOR_FOLDER = "/"
    private const val URL_SEPARATOR_NUMBER = "-"
    private const val URL_FILE_EXT_PNG = ".png"
}
