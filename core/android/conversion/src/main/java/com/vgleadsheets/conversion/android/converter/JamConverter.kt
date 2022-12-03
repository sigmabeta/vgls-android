package com.vgleadsheets.conversion.android.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.conversion.OneToOneConverter
import com.vgleadsheets.database.android.dao.SongRoomDao
import com.vgleadsheets.database.android.enitity.JamEntity
import com.vgleadsheets.database.android.enitity.SongEntity
import com.vgleadsheets.model.Jam
import com.vgleadsheets.model.Song

class JamConverter :
    OneToOneConverter<Jam, JamEntity, Song, SongEntity, SongRoomDao> {
    fun toModelFull(
        entity: JamEntity,
        songRoomDao: SongRoomDao,
        converterSongs: Converter<Song, SongEntity>,
    ) = Jam(
        entity.id,
        entity.name,
        entity.currentSheetId,
        songRoomDao.getForeignModel(entity.currentSheetId ?: -1L, converterSongs)
    )

    override fun Jam.toEntity() = JamEntity(
        id,
        name,
        currentSongId
    )

    override fun JamEntity.toModel() = Jam(
        id,
        name,
        currentSheetId,
        null,
    )

    override fun JamEntity.toModelWithRelatedOne(
        foreignDao: SongRoomDao,
        converter: Converter<Song, SongEntity>
    ) = Jam(
        id,
        name,
        currentSheetId,
        foreignDao.getForeignModel(currentSheetId ?: -1L, converter),
    )

    override fun SongRoomDao.getForeignModel(
        foreignId: Long,
        converter: Converter<Song, SongEntity>
    ) = converter.entityToModel(
        getOneByIdSync(foreignId)
    )
}
