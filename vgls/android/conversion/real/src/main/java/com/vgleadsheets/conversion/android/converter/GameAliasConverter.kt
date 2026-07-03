package com.vgleadsheets.conversion.android.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.database.android.enitity.GameAliasEntity
import com.vgleadsheets.model.alias.GameAlias

class GameAliasConverter : Converter<GameAlias, GameAliasEntity> {
    override fun GameAlias.toEntity() = GameAliasEntity(
        gameId,
        name,
        id
    )

    override fun GameAliasEntity.toModel() = GameAlias(
        id ?: -1L,
        gameId,
        name,
        null
    )
}
