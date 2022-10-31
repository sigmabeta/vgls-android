package com.vgleadsheets.conversion.android.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.conversion.WithManyConverter
import com.vgleadsheets.database.android.dao.ComposerRoomDao
import com.vgleadsheets.database.android.enitity.ComposerEntity
import com.vgleadsheets.database.android.enitity.SongEntity
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Song

class SongConverter :
    WithManyConverter<Song, SongEntity, Composer, ComposerEntity, ComposerRoomDao> {
    override fun toModelFromEntity(entity: SongEntity) = entity.toModel()

    override fun Song.toEntity() = SongEntity(
        id,
        name,
        filename,
        pageCount,
        lyricPageCount,
        gameName,
        gameId,
        hasVocals,
        playCount
    )

    override fun SongEntity.toModel() = Song(
        id,
        name,
        filename,
        game_id,
        gameName,
        hasVocals,
        pageCount,
        lyricPageCount,
        null,
        playCount
    )

    override fun SongEntity.toModelWithJoinedMany(
        manyDao: ComposerRoomDao,
        converter: Converter<Composer, ComposerEntity>
    ) = Song(
        id,
        name,
        filename,
        game_id,
        gameName,
        hasVocals,
        pageCount,
        lyricPageCount,
        manyDao.getJoinedModels(id, converter),
        playCount
    )

    override fun ComposerRoomDao.getJoinedModels(
        relationId: Long,
        converter: Converter<Composer, ComposerEntity>
    ) = getJoinedEntitiesSync(relationId)
        .map { converter.entityToModel(it) }
}
