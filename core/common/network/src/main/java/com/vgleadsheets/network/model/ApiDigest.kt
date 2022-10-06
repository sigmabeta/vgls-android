package com.vgleadsheets.network.model

data class ApiDigest(
    val composers: List<ApiComposer>,
    val games: List<VglsApiGame>
)
