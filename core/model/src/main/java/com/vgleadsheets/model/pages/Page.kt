package com.vgleadsheets.model.pages

import android.net.Uri
import com.vgleadsheets.model.parts.Part

object Page {
    fun generateImageUrl(
        baseImageUrl: String,
        part: Part,
        filename: String,
        pageNumber: Int
    ): String {
        return baseImageUrl +
            part.apiId + URL_SEPARATOR_FOLDER +
            Uri.encode(filename) + URL_SEPARATOR_NUMBER +
            pageNumber + URL_FILE_EXT_PNG
    }

    private const val URL_SEPARATOR_FOLDER = "/"
    private const val URL_SEPARATOR_NUMBER = "-"
    private const val URL_FILE_EXT_PNG = ".png"
}
