package com.vgleadsheets.model.game

import com.vgleadsheets.model.song.ApiSong

data class ApiGame(
    val game_id: Long,
    val game_name: String,
    val songs: List<ApiSong>) {
    fun toGameEntity() = GameEntity(game_id, game_name)
}