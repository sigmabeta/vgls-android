package com.vgleadsheets.database.android.dao.wrapper

import com.vgleadsheets.model.song.Song
import kotlinx.coroutines.flow.Flow

class SongRoomDaoWrapper(
    private val roomImpl: RoomDao
): Dao {
    override fun getAll(): Flow<List<Song>>

    override fun getSongsForGame(gameId: Long): Flow<List<Song>>

    override fun getSongsForGameSync(gameId: Long): List<Song>

    override fun getSong(songId: Long): Flow<Song>

    override fun getSongSync(songId: Long): Song?

    override fun searchSongsByTitle(title: String): Flow<List<Song>>

    override suspend fun insertAll(songs: List<Song>)

    override suspend fun nukeTable()
}
