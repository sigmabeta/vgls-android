package com.vgleadsheets.repository

import com.vgleadsheets.conversion.mapListTo
import com.vgleadsheets.database.dao.ComposerDataSource
import com.vgleadsheets.database.dao.SongDataSource
import com.vgleadsheets.database.source.OfflineComposerDataSource
import com.vgleadsheets.database.source.OfflineGameDataSource
import com.vgleadsheets.database.source.OfflineSongDataSource
import kotlinx.coroutines.flow.map

class OfflineRepository(
    private val songDataSource: SongDataSource,
    private val composerDataSource: ComposerDataSource,
    private val offlineSongDataSource: OfflineSongDataSource,
    private val offlineComposerDataSource: OfflineComposerDataSource,
    private val offlineGameDataSource: OfflineGameDataSource,
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

    suspend fun addOfflineComposer(id: Long) {
        offlineComposerDataSource.addOffline(id)
    }

    suspend fun removeOfflineComposer(id: Long) {
        offlineComposerDataSource.removeOffline(id)
    }

    fun getAllComposers() = offlineComposerDataSource
        .getAll()
        .mapListTo {
            composerDataSource.getOneByIdSync(it.id)
        }

    fun isOfflineComposer(id: Long) = offlineComposerDataSource.isOfflineComposer(id)

    suspend fun addOfflineGame(id: Long) {
        offlineGameDataSource.addOffline(id)
    }

    suspend fun removeOfflineGame(id: Long) {
        offlineGameDataSource.removeOffline(id)
    }

    fun isOfflineGame(id: Long) = offlineGameDataSource.isOfflineGame(id)

    fun getAllGameSongs() = offlineGameDataSource
        .getAll()
        .map { games ->
            games.flatMap { offline ->
                songDataSource.getSongsForGameSync(offline.id)
            }
        }

    fun getAllComposerSongs() = offlineComposerDataSource
        .getAll()
        .map { composers ->
            composers.flatMap { offline ->
                songDataSource.getSongsForComposerSync(offline.id)
            }
        }
}
