package com.vgleadsheets.conversion.android.datasource

import com.vgleadsheets.conversion.android.AndroidDataSource
import com.vgleadsheets.conversion.android.converter.SongConverter
import com.vgleadsheets.conversion.mapListTo
import com.vgleadsheets.database.android.dao.SongRoomDao
import com.vgleadsheets.database.android.enitity.SongEntity
import com.vgleadsheets.database.dao.SongDataSource
import com.vgleadsheets.model.Song

class SongAndroidDataSource(
    private val convert: SongConverter,
    private val roomImpl: SongRoomDao,
) : AndroidDataSource<SongRoomDao, Song, SongEntity, SongConverter>(
    convert,
    roomImpl,
),
SongDataSource {
    override fun getFavorites() = roomImpl
        .getFavorites()
        .mapListTo { convert.entityToModel(it) }

    override fun searchByName(name: String) = roomImpl
        .searchByName(name)
        .mapListTo { convert.entityToModel(it) }

    override fun getSongsForGame(gameId: Long) = roomImpl
        .getForGame(gameId)
        .mapListTo { convert.entityToModel(it) }

    override fun getSongsForGameSync(gameId: Long) = roomImpl
        .getForGameSync(gameId)
        .map { convert.entityToModel(it) }

    override fun getSongsForComposer(composerId: Long) = roomImpl
        .getForComposer(composerId)
        .mapListTo { convert.entityToModel(it) }

    override fun getSongsForTagValue(tagValueId: Long) = roomImpl
        .getForTagValue(tagValueId)
        .mapListTo { convert.entityToModel(it) }

    override fun incrementPlayCount(songId: Long) = roomImpl.incrementPlayCount(songId)

    override fun toggleFavorite(songId: Long) = roomImpl.toggleFavorite(songId)

    override fun toggleOffline(songId: Long) = roomImpl.toggleOffline(songId)

    override fun toggleAlternate(songId: Long) = roomImpl.toggleAlternate(songId)
}
