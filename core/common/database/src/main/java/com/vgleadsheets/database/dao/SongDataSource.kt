package com.vgleadsheets.database.dao

import com.vgleadsheets.model.Song
import kotlinx.coroutines.flow.Flow

interface SongDataSource : DataSource<Song> {
    fun getFavorites(): Flow<List<Song>>

    fun searchByName(name: String): Flow<List<Song>>

    fun getSongsForGame(gameId: Long): Flow<List<Song>>

    fun getSongsForGameSync(gameId: Long): List<Song>

    fun getSongsForComposer(composerId: Long): Flow<List<Song>>

    fun getSongsForTagValue(tagValueId: Long): Flow<List<Song>>

    fun incrementPlayCount(songId: Long)

    fun toggleFavorite(songId: Long)

    fun toggleOffline(songId: Long)

    fun toggleAlternate(songId: Long)

}
