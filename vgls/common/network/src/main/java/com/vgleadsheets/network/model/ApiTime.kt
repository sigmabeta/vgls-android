package com.vgleadsheets.network.model

import com.squareup.moshi.JsonClass

@Suppress("ConstructorParameterNaming")
@JsonClass(generateAdapter = true)
data class ApiTime(
    val last_updated: String
)
