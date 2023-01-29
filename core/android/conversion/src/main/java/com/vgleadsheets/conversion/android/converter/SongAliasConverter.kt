package com.vgleadsheets.conversion.android.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.conversion.OneToOneConverter
import com.vgleadsheets.database.android.dao.SongRoomDao
import com.vgleadsheets.database.android.enitity.SongAliasEntity
import com.vgleadsheets.database.android.enitity.SongEntity
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.alias.SongAlias

class SongAliasConverter :
    OneToOneConverter<SongAlias, SongAliasEntity, Song, SongEntity, SongRoomDao> {
    override fun SongAlias.toEntity() = SongAliasEntity(
        songId,
        name,
        id
    )

    override fun SongAliasEntity.toModel() = SongAlias(
        id ?: -1L,
        songId,
        name,
        null
    )

    override fun SongAliasEntity.toModelWithRelatedOne(
        foreignDao: SongRoomDao,
        converter: Converter<Song, SongEntity>
    ) = SongAlias(
        id ?: -1L,
        songId,
        name,
        foreignDao.getForeignModel(songId, converter)
    )

    override fun SongRoomDao.getForeignModel(
        foreignId: Long,
        converter: Converter<Song, SongEntity>
    ) = getOneByIdSync(foreignId)
        .let { converter.entityToModel(it) }
}
