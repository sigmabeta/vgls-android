package com.vgleadsheets.repository

import com.vgleadsheets.conversion.mapListTo
import com.vgleadsheets.database.dao.SongDataSource
import com.vgleadsheets.database.source.OfflineSongDataSource

class OfflineRepository(
    private val songDataSource: SongDataSource,
    private val offlineSongDataSource: OfflineSongDataSource,
) {
    suspend fun addOfflineSong(id: Long) {
        offlineSongDataSource.addOffline(id)
    }

    suspend fun removeOfflineSong(id: Long) {
        offlineSongDataSource.removeOffline(id)
    }

    fun getAllSongs() = offlineSongDataSource
        .getAll()
        .mapListTo {
            songDataSource.getOneByIdSync(it.id)
        }

    fun isOfflineSong(id: Long) = offlineSongDataSource.isOfflineSong(id)
}
