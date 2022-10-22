package com.vgleadsheets.conversion.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.database.enitity.GameAliasEntity
import com.vgleadsheets.model.alias.GameAlias

class GameAliasConverter :
    Converter<GameAlias, GameAliasEntity> {
    override fun GameAlias.toEntity() = GameAliasEntity(
        id,
        name,
        photoUrl
    )

    override fun GameAliasEntity.toModel() = GameAlias(
        id ?: -1L,
        gameId,
        name,
        photoUrl
    )
}
