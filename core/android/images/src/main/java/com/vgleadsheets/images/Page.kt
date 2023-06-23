package com.vgleadsheets.images

import com.vgleadsheets.url.UrlGenerator

object Page {
    fun generateImageUrl(
        baseImageUrl: String,
        partApiId: String,
        filename: String,
        isAlternateEnabled: Boolean,
        pageNumber: Int
    ) = UrlGenerator.generateSongPageImageUrl(
        baseImageUrl,
        partApiId,
        filename,
        isAlternateEnabled,
        pageNumber,
    )

    fun generateThumbUrl(
        baseImageUrl: String,
        selectedPartApiId: String,
        isAlternateEnabled: Boolean,
        filename: String
    ) = UrlGenerator.generateSongPageImageUrl(
        baseImageUrl,
        selectedPartApiId,
        filename,
        isAlternateEnabled,
        1
    )
}
