package com.vgleadsheets.network.model

@Suppress("ConstructorParameterNaming")
data class ApiComposer(
    val aliases: List<String>?,
    val composer_id: Long,
    val composer_name: String?,
    val image_url: String?
)
