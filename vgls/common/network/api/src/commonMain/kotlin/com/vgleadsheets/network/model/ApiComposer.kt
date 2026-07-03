package com.vgleadsheets.network.model

import kotlinx.serialization.Serializable

@Suppress("ConstructorParameterNaming")
@Serializable
data class ApiComposer(
    val aliases: List<String>? = null,
    val composer_id: Long,
    val composer_name: String? = null,
    val image_url: String? = null
)
