package com.vgleadsheets.model

/**
 * A composer's photo from vgm-metadata and the credit its license requires. [url] is the photo
 * itself; the rest are null when the source doesn't report them.
 */
data class ComposerPhoto(
    val url: String,
    val author: String? = null,
    val license: String? = null,
    val licenseUrl: String? = null,
    val sourceUrl: String? = null,
)
