package com.vgleadsheets.repository

import com.vgleadsheets.network.model.VglsApiGame

private var firstPull = true
private var divisor = if (firstPull) 2 else 1

fun List<VglsApiGame>.takeHalfTheSongsFirstTime() = map {
    firstPull = false
    val songCount = Integer.max(it.songs.size / divisor, 1)

    val songs = it.songs.take(songCount)
    val game = VglsApiGame(
        it.aliases,
        it.game_id,
        it.game_name,
        songs,
        it.image_url
    )
    game
}

