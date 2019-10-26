package com.vgleadsheets.model.game

import com.vgleadsheets.model.song.ApiSong

@Suppress("ConstructorParameterNaming")
data class VglsApiGame(
    val game_id: Long,
    val game_name: String,
    val songs: List<ApiSong>
) {
    fun toGameEntity() = GameEntity(game_id + ID_OFFSET, game_name)

    companion object {
        const val ID_OFFSET = 100000L
    }
}
