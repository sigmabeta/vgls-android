package com.vgleadsheets.database.dao

import com.vgleadsheets.model.song.Song
import kotlinx.coroutines.flow.Flow

interface SongDao {

    fun getAll(): Flow<List<Song>>

    fun getSongsForGame(gameId: Long): Flow<List<Song>>

    fun getSongsForGameSync(gameId: Long): List<Song>

    fun getSong(songId: Long): Flow<Song>

    fun getSongSync(songId: Long): Song?

    fun searchSongsByTitle(title: String): Flow<List<Song>>

    suspend fun insertAll(songs: List<Song>)

    suspend fun nukeTable()
}
