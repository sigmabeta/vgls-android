package com.vgleadsheets.conversion.android.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.database.android.enitity.GameEntity
import com.vgleadsheets.model.Game

class GameConverter : Converter<Game, GameEntity> {
    override fun Game.toEntity() = GameEntity(
        id,
        name,
        hasVocalSongs,
        photoUrl,
        songCount,
        sheetsPlayed,
        isFavorite,
        isAvailableOffline
    )

    override fun GameEntity.toModel() = Game(
        id,
        name,
        null,
        hasVocalSongs,
        songCount,
        photoUrl,
        sheetsPlayed,
        isFavorite,
        isAvailableOffline,
    )
}
