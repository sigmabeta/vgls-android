package com.vgleadsheets.network.model

import kotlinx.serialization.Serializable

@Suppress("ConstructorParameterNaming")
@Serializable
data class ApiTime(
    val last_updated: String
)
