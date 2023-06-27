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

    fun generateSongLinkUrl(
        gameName: String,
        songName: String
    ) = URL_VGLS_SONG_PATH +
        processUrl(gameName) +
        URL_SEPARATOR_FOLDER +
        processUrl(songName)

    private fun processUrl(urlToProcess: String) =
         urlToProcess
            .replace(" ", "-")
            .replace("é", "")
            .replace("&", "")
            .replace("'", "")
            .replace(",", "")
            .replace("!", "")
            .replace(".", "")
            .replace(":", "")
            .replace("--", "-")

    private const val IDENTIFIER_ALT = " [ALT]"

    private const val URL_SEPARATOR_FOLDER = "/"
    private const val URL_SEPARATOR_NUMBER = "-"
    private const val URL_FILE_EXT_PNG = ".png"
    private const val URL_VGLS_SONG_PATH = "https://www.vgleadsheets.com/view/"
}
