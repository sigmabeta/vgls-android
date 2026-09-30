package com.vgleadsheets.model

/** Builds and parses IGDB image URLs (https://api-docs.igdb.com/#images). */
object IgdbImages {
    private const val BASE = "https://images.igdb.com/igdb/image/upload"

    // IGDB's `cover_big_2x` size, 528×748: sharp in grid cells and the game detail hero alike.
    private const val COVER_SIZE = "t_cover_big_2x"

    private val IMAGE_ID_REGEX = Regex("""/t_[^/]+/([A-Za-z0-9_-]+)\.[a-z]+$""")

    fun coverUrl(imageId: String): String = "$BASE/$COVER_SIZE/$imageId.jpg"

    /** The image id in an IGDB image URL of any size, e.g. `co3plw`; null if it isn't one. */
    fun imageIdFromUrl(url: String): String? = IMAGE_ID_REGEX.find(url)?.groupValues?.get(1)
}
