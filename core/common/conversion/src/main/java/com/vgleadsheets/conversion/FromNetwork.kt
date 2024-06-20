package com.vgleadsheets.conversion

import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song
import com.vgleadsheets.network.model.ApiComposer
import com.vgleadsheets.network.model.ApiSong
import com.vgleadsheets.network.model.VglsApiGame

fun VglsApiGame.asModel(
    sheetsPlayed: Int,
    isFavorite: Boolean,
    isAvailableOffline: Boolean,
    hasVocalSongs: Boolean,
    songCount: Int
) = Game(
    game_id,
    game_name,
    null,
    hasVocalSongs,
    songCount,
    image_url,
    sheetsPlayed,
    isFavorite,
    isAvailableOffline
)

fun ApiSong.asModel(
    gameId: Long,
    gameName: String,
    playCount: Int,
    isFavorite: Boolean,
    isAvailableOffline: Boolean,
    isAltSelected: Boolean
) = Song(
    id,
    name,
    filename,
    gameId,
    gameName,
    null,
    lyricsPageCount > 0,
    pageCount,
    altPageCount,
    lyricsPageCount,
    null,
    playCount,
    isFavorite,
    isAvailableOffline,
    isAltSelected
)

fun ApiComposer.asModel(
    sheetsPlayed: Int,
    isFavorite: Boolean,
    isAvailableOffline: Boolean,
    hasVocalSongs: Boolean,
    songCount: Int
) = Composer(
    composer_id,
    composer_name ?: "Unknown Composer",
    null,
    songCount,
    image_url,
    hasVocalSongs,
    sheetsPlayed,
    isFavorite,
    isAvailableOffline
)
