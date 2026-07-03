package com.vgleadsheets.network.model

import kotlinx.serialization.Serializable

@Suppress("ConstructorParameterNaming")
@Serializable
data class VglsApiGame(
    val aliases: List<String>? = null,
    val game_id: Long,
    val game_name: String,
    val songs: List<ApiSong>,
    val image_url: String? = null
)
