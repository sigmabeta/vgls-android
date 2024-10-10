package com.vgleadsheets.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiDigest(
    val composers: List<ApiComposer>,
    val games: List<VglsApiGame>
)
