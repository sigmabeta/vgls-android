package com.vgleadsheets.conversion

import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song
import com.vgleadsheets.network.model.ApiComposer
import com.vgleadsheets.network.model.ApiSong
import com.vgleadsheets.network.model.VglsApiGame

fun VglsApiGame.toModel(
    sheetsPlayed: Int,
    isFavorite: Boolean,
    isAvailableOffline: Boolean
) = Game(
    game_id,
    game_name,
    null,
    image_url,
    sheetsPlayed,
    isFavorite,
    isAvailableOffline
)

fun ApiSong.toModel(
    gameId: Long,
    gameName: String,
    playCount: Int,
    isFavorite: Boolean,
    isAvailableOffline: Boolean
) = Song(
    id,
    name,
    filename,
    gameId,
    gameName,
    lyricsPageCount > 0,
    pageCount,
    lyricsPageCount,
    null,
    playCount,
    isFavorite,
    isAvailableOffline
)

fun ApiComposer.toModel(sheetsPlayed: Int) = Composer(
    composer_id,
    composer_name ?: "Unknown Composer",
    null,
    image_url,
    false,
    sheetsPlayed
)
