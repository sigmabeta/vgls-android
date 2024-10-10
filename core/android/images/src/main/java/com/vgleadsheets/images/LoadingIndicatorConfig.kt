package com.vgleadsheets.images

import kotlinx.collections.immutable.ImmutableList

data class LoadingIndicatorConfig(
    val title: String,
    val gameName: String,
    val composers: ImmutableList<String>,
    val pageNumber: Int,
)
