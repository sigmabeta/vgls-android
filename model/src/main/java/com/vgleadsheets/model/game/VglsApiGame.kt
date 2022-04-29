package com.vgleadsheets.model.game

import com.vgleadsheets.model.song.ApiSong

@Suppress("ConstructorParameterNaming")
data class VglsApiGame(
    val aliases: List<String>?,
    val game_id: Long,
    val game_name: String,
    val songs: List<ApiSong>,
    val image_url: String?
) {
    fun toGameEntity() = GameEntity(
        game_id + ID_OFFSET,
        game_name,
        songs.hasVocalSongs(),
        image_url
    )

    private fun List<ApiSong>.hasVocalSongs() = firstOrNull { it.parts.contains("Vocals") } != null

    companion object {
        const val ID_OFFSET = 100000L
    }
}
