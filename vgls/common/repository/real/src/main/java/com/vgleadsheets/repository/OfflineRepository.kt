package com.vgleadsheets.repository

import com.vgleadsheets.conversion.mapListTo
import com.vgleadsheets.database.dao.ComposerDataSource
import com.vgleadsheets.database.dao.DbStatisticsDataSource
import com.vgleadsheets.database.dao.GameDataSource
import com.vgleadsheets.database.dao.SongDataSource
import com.vgleadsheets.database.source.OfflineComposerDataSource
import com.vgleadsheets.database.source.OfflineGameDataSource
import com.vgleadsheets.database.source.OfflineSongDataSource
import com.vgleadsheets.database.source.OfflineUpdateResultDataSource
import com.vgleadsheets.model.time.TimeType
import com.vgleadsheets.model.updates.OfflineJobStatus
import com.vgleadsheets.model.updates.OfflineUpdateResult
import net.sigmabeta.sage.time.TimeProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.time.Instant

class OfflineRepository(
    private val songDataSource: SongDataSource,
    private val composerDataSource: ComposerDataSource,
    private val gameDataSource: GameDataSource,
    private val offlineSongDataSource: OfflineSongDataSource,
    private val offlineComposerDataSource: OfflineComposerDataSource,
    private val offlineGameDataSource: OfflineGameDataSource,
    private val offlineUpdateResultDataSource: OfflineUpdateResultDataSource,
    private val dbStatisticsDataSource: DbStatisticsDataSource,
    private val threeTenTime: TimeProvider,
) {
    suspend fun addOfflineSong(id: Long) {
        offlineSongDataSource.addOffline(id)
    }

    suspend fun setSongLastDownloaded(songId: Long, timestamp: Long) {
        songDataSource.setLastDownloaded(songId, timestamp)
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

    fun getAllGames() = offlineGameDataSource
        .getAll()
        .mapListTo {
            gameDataSource.getOneByIdSync(it.id)
        }

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

    fun getAllUpdateResults() = offlineUpdateResultDataSource.getAll()

    suspend fun insertUpdateResult(successfulOfflines: Int, status: OfflineJobStatus) {
        val serverUpdateTimeMs = dbStatisticsDataSource
            .getTime(TimeType.LAST_VGLS_UPDATE.ordinal)
            .first()
            .timeMs
        val serverUpdateTime = Instant.fromEpochMilliseconds(serverUpdateTimeMs)

        offlineUpdateResultDataSource.insert(
            OfflineUpdateResult(
                id = 0,
                dateTime = threeTenTime.now(),
                serverUpdateTime = serverUpdateTime,
                updatedSongs = 0,
                successfulOfflines = successfulOfflines,
                status = status,
            )
        )
    }
}
