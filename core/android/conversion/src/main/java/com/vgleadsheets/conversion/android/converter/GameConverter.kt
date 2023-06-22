package com.vgleadsheets.conversion.android.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.database.android.enitity.GameEntity
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Part
import com.vgleadsheets.model.filteredForVocals

class GameConverter : Converter<Game, GameEntity> {
    override fun Game.toEntity() = GameEntity(
        id,
        name,
        songs?.filteredForVocals(Part.VOCAL.apiId)?.isNotEmpty() ?: false,
        photoUrl,
        sheetsPlayed,
        isFavorite,
        isAvailableOffline
    )

    override fun GameEntity.toModel() = Game(
        id,
        name,
        null,
        photoUrl,
        sheetsPlayed,
        isFavorite,
        isAvailableOffline,
    )
}
