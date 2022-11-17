package com.vgleadsheets.conversion

import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song
import com.vgleadsheets.network.model.ApiComposer
import com.vgleadsheets.network.model.ApiSong
import com.vgleadsheets.network.model.VglsApiGame

fun VglsApiGame.toModel() = Game(
    game_id,
    game_name,
    null,
    image_url
)

fun ApiSong.toModel(gameId: Long, gameName: String) = Song(
    id,
    name,
    filename,
    gameId,
    gameName,
    lyricsPageCount > 0,
    pageCount,
    lyricsPageCount,
    null,
    0
)

fun ApiComposer.toModel() = Composer(
    composer_id,
    composer_name ?: "Unknown Composer",
    null,
    null,
    false
)
