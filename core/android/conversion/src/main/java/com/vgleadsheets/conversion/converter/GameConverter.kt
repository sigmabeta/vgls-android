package com.vgleadsheets.conversion.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.conversion.OneToManyConverter
import com.vgleadsheets.database.android.dao.SongRoomDao
import com.vgleadsheets.database.enitity.GameEntity
import com.vgleadsheets.database.enitity.SongEntity
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Part
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.filteredForVocals

class GameConverter : OneToManyConverter<Game, GameEntity, Song, SongEntity, SongRoomDao> {
    override fun Game.toEntity() = GameEntity(
        id,
        name,
        songs?.filteredForVocals(Part.VOCAL.apiId)?.isNotEmpty() ?: false,
        photoUrl
    )

    override fun GameEntity.toModel() = Game(
        id,
        name,
        null,
        photoUrl
    )

    override fun GameEntity.toModelWithRelatedMany(
        manyDao: SongRoomDao,
        converter: Converter<Song, SongEntity>
    ): Game = Game(
        id,
        name,
        manyDao.getRelatedModels(id, converter),
        photoUrl
    )

    override fun SongRoomDao.getRelatedModels(
        relationId: Long,
        converter: Converter<Song, SongEntity>
    ) = getRelatedEntitiesSync(relationId).map { converter.entityToModel(it) }
}
