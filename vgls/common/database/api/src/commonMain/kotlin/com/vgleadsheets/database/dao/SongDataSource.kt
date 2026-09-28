package com.vgleadsheets.database.dao

import com.vgleadsheets.model.Song
import kotlinx.coroutines.flow.Flow

interface SongDataSource : DataSource<Song> {
    fun getFavorites(): Flow<List<Song>>

    fun searchByName(name: String): Flow<List<Song>>

    fun getSongsForGame(gameId: Long): Flow<List<Song>>

    suspend fun getSongsForGameSync(gameId: Long): List<Song>

    fun getSongsForComposer(composerId: Long): Flow<List<Song>>

    suspend fun getSongsForComposerSync(composerId: Long): List<Song>

    fun getSongsForTagValue(tagValueId: Long): Flow<List<Song>>

    suspend fun incrementPlayCount(songId: Long)

    suspend fun toggleFavorite(songId: Long)

    suspend fun toggleOffline(songId: Long)

    suspend fun setLastDownloaded(songId: Long, timestamp: Long)

    fun getHighestId(): Flow<Long>
}
