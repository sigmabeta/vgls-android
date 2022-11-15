package com.vgleadsheets.conversion.android.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.conversion.OneToOneConverter
import com.vgleadsheets.database.android.dao.SongRoomDao
import com.vgleadsheets.database.android.enitity.SongEntity
import com.vgleadsheets.database.android.enitity.SongHistoryEntryEntity
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.SongHistoryEntry

class SongHistoryEntryConverter :
    OneToOneConverter<SongHistoryEntry, SongHistoryEntryEntity, Song, SongEntity, SongRoomDao> {
    override fun SongHistoryEntry.toEntity() = SongHistoryEntryEntity(
        id,
        jamId,
        songId
    )

    override fun SongHistoryEntryEntity.toModel() = SongHistoryEntry(
        id,
        song_id,
        jam_id,
        null
    )

    override fun SongHistoryEntryEntity.toModelWithRelatedOne(
        foreignDao: SongRoomDao,
        converter: Converter<Song, SongEntity>
    ) = SongHistoryEntry(
        id,
        song_id,
        jam_id,
        foreignDao.getForeignModel(song_id, converter)
    )

    override fun SongRoomDao.getForeignModel(
        foreignId: Long,
        converter: Converter<Song, SongEntity>
    ) = converter.entityToModel(
        getOneByIdSync(foreignId)
    )
}
