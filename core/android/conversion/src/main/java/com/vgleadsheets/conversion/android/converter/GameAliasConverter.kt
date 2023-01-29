package com.vgleadsheets.conversion.android.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.conversion.OneToOneConverter
import com.vgleadsheets.database.android.dao.GameRoomDao
import com.vgleadsheets.database.android.enitity.GameAliasEntity
import com.vgleadsheets.database.android.enitity.GameEntity
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.alias.GameAlias

class GameAliasConverter :
    OneToOneConverter<GameAlias, GameAliasEntity, Game, GameEntity, GameRoomDao> {
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

    override fun GameAliasEntity.toModelWithRelatedOne(
        foreignDao: GameRoomDao,
        converter: Converter<Game, GameEntity>
    ) = GameAlias(
        id ?: -1L,
        gameId,
        name,
        foreignDao.getForeignModel(gameId, converter)
    )

    override fun GameRoomDao.getForeignModel(
        foreignId: Long,
        converter: Converter<Game, GameEntity>
    ) = getOneByIdSync(foreignId)
        .let { converter.entityToModel(it) }
}
