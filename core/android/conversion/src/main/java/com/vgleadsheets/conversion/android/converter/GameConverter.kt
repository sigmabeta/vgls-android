package com.vgleadsheets.conversion.android.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.conversion.WithManyConverter
import com.vgleadsheets.database.android.dao.SongRoomDao
import com.vgleadsheets.database.android.enitity.GameEntity
import com.vgleadsheets.database.android.enitity.SongEntity
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Part
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.filteredForVocals

class GameConverter : WithManyConverter<Game, GameEntity, Song, SongEntity, SongRoomDao> {
    override fun Game.toEntity() = GameEntity(
        id,
        name,
        songs?.filteredForVocals(Part.VOCAL.apiId)?.isNotEmpty() ?: false,
        photoUrl,
        sheetsPlayed
    )

    override fun GameEntity.toModel() = Game(
        id,
        name,
        null,
        photoUrl,
        sheetsPlayed
    )

    override fun GameEntity.toModelWithMany(
        manyDao: SongRoomDao,
        converter: Converter<Song, SongEntity>
    ): Game = Game(
        id,
        name,
        manyDao.getManyModels(id, converter),
        photoUrl,
        sheetsPlayed
    )

    override fun SongRoomDao.getManyModels(
        relationId: Long,
        converter: Converter<Song, SongEntity>
    ) = getEntitiesForForeignSync(relationId).map { converter.entityToModel(it) }
}
