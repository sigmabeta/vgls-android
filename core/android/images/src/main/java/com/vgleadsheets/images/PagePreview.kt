package com.vgleadsheets.images

import kotlinx.collections.immutable.ImmutableList

data class PagePreview(
    val title: String,
    val transposition: String,
    val gameName: String,
    val composers: ImmutableList<String>,
    val pageNumber: Int,
)
