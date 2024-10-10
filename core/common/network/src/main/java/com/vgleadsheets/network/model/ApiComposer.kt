package com.vgleadsheets.network.model

import com.squareup.moshi.JsonClass

@Suppress("ConstructorParameterNaming")
@JsonClass(generateAdapter = true)
data class ApiComposer(
    val aliases: List<String>?,
    val composer_id: Long,
    val composer_name: String?,
    val image_url: String?
)
