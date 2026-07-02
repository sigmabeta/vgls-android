package com.vgleadsheets.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiDigest(
    val composers: List<ApiComposer>,
    val games: List<VglsApiGame>
)
