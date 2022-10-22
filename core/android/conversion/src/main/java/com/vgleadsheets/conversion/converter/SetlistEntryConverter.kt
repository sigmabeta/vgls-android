package com.vgleadsheets.conversion.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.conversion.OneToOneConverter
import com.vgleadsheets.database.android.dao.SongRoomDao
import com.vgleadsheets.database.enitity.SetlistEntryEntity
import com.vgleadsheets.database.enitity.SongEntity
import com.vgleadsheets.model.SetlistEntry
import com.vgleadsheets.model.Song

class SetlistEntryConverter :
    OneToOneConverter<SetlistEntry, SetlistEntryEntity, Song, SongEntity, SongRoomDao> {
    override fun SetlistEntry.toEntity() = SetlistEntryEntity(
        id,
        gameName,
        songName,
        jamId,
        song!!.id
    )

    override fun SetlistEntryEntity.toModel() = SetlistEntry(
        id,
        jam_id,
        game_name,
        song_name,
        null
    )

    override fun SetlistEntryEntity.toModelWithRelatedOne(
        foreignDao: SongRoomDao,
        converter: Converter<Song, SongEntity>
    ) = SetlistEntry(
        id,
        jam_id,
        game_name,
        song_name,
        foreignDao.getForeignModel(song_id, converter)
    )

    override fun SongRoomDao.getForeignModel(
        foreignId: Long,
        converter: Converter<Song, SongEntity>
    ) = converter.entityToModel(
        getOneByIdSync(foreignId)
    )
}
