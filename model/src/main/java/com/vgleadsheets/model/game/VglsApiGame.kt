package com.vgleadsheets.model.game

import com.vgleadsheets.model.song.ApiSong

@Suppress("ConstructorParameterNaming")
data class VglsApiGame(
    val game_id: Long,
    val game_name: String,
    val songs: List<ApiSong>
) {
    fun toGameEntity() = GameEntity(game_id, game_name)
}
