package com.vgleadsheets.network.model

import com.vgleadsheets.model.Composer

@Suppress("ConstructorParameterNaming")
data class ApiComposer(
    val aliases: List<String>?,
    val composer_id: Long,
    val composer_name: String?,
    val image_url: String?
)

fun ApiComposer.toComposer(hasVocalSongs: Boolean) = Composer(
    composer_id + ID_OFFSET_COMPOSER,
    composer_name ?: "Unknown Composer",
    null,
    image_url
)

const val ID_OFFSET_COMPOSER = 1000000000L
