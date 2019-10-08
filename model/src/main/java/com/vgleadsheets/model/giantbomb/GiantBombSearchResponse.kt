package com.vgleadsheets.model.giantbomb

data class GiantBombSearchResponse<T>(
    val error: String,
    val results: List<T>
)
