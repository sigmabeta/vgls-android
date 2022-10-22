package com.vgleadsheets.conversion.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.conversion.OneToManyConverter
import com.vgleadsheets.conversion.OneToOneConverter
import com.vgleadsheets.database.android.dao.SongHistoryEntryRoomDao
import com.vgleadsheets.database.android.dao.SongRoomDao
import com.vgleadsheets.database.enitity.JamEntity
import com.vgleadsheets.database.enitity.SongEntity
import com.vgleadsheets.database.enitity.SongHistoryEntryEntity
import com.vgleadsheets.model.Jam
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.SongHistoryEntry

class JamConverter :
    OneToOneConverter<Jam, JamEntity, Song, SongEntity, SongRoomDao>,
    OneToManyConverter<Jam, JamEntity, SongHistoryEntry, SongHistoryEntryEntity, SongHistoryEntryRoomDao> {
    fun toModelFull(
        entity: JamEntity,
        songRoomDao: SongRoomDao,
        songHistoryEntryRoomDao: SongHistoryEntryRoomDao,
        converterSongs: Converter<Song, SongEntity>,
        converterSongHistory: Converter<SongHistoryEntry, SongHistoryEntryEntity>
    ) = Jam(
        entity.id,
        entity.name,
        songRoomDao.getForeignModel(entity.id, converterSongs),
        songHistoryEntryRoomDao.getRelatedModels(entity.id, converterSongHistory)
    )

    override fun Jam.toEntity() = JamEntity(
        id,
        name,
        currentSong?.id
    )

    override fun JamEntity.toModel() = Jam(
        id,
        name,
        null,
        null
    )

    override fun JamEntity.toModelWithRelatedOne(
        foreignDao: SongRoomDao,
        converter: Converter<Song, SongEntity>
    ) = Jam(
        id,
        name,
        foreignDao.getForeignModel(id, converter),
        null
    )

    override fun JamEntity.toModelWithRelatedMany(
        manyDao: SongHistoryEntryRoomDao,
        converter: Converter<SongHistoryEntry, SongHistoryEntryEntity>
    ) = Jam(
        id,
        name,
        null,
        manyDao.getRelatedModels(id, converter)
    )

    override fun SongRoomDao.getForeignModel(
        foreignId: Long,
        converter: Converter<Song, SongEntity>
    ) = converter.entityToModel(
        getOneByIdSync(foreignId)
    )

    override fun SongHistoryEntryRoomDao.getRelatedModels(
        relationId: Long,
        converter: Converter<SongHistoryEntry, SongHistoryEntryEntity>
    ) = getAllForJamSync(relationId)
        .map { converter.entityToModel(it) }
}
