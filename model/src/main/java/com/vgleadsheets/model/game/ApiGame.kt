package com.vgleadsheets.model.game

import com.vgleadsheets.model.song.ApiSong

data class ApiGame(
    val game_name: String,
    val songs: List<ApiSong>) {
    fun toGame() = GameEntity(game_name)
}