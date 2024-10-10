package com.vgleadsheets.repository.history

import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.database.dao.SongDataSource
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.model.Song
import com.vgleadsheets.settings.GeneralSettingsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take

class UserContentMigrator(
    private val songHistoryRepository: SongHistoryRepository,
    private val songDataSource: SongDataSource,
    private val settingsManager: GeneralSettingsManager,
    private val hatchet: Hatchet,
    private val coroutineScope: CoroutineScope,
    private val dispatchers: VglsDispatchers,
) {
    init {
        autoMigrateIfNecessary()
    }

    fun migrateUserData() = songDataSource
        .getAll()
        .filter { it.isNotEmpty() }
        .take(1)
        .map { processSongs(it) }

    private fun autoMigrateIfNecessary() {
        settingsManager.getNeedsAutoMigrate()
            .take(1)
            .flatMapLatest { needsMigrate ->
                if (needsMigrate != false) {
                    migrateUserData()
                } else {
                    flow { }
                }
            }
            .onEach { songsMigrated ->
                if (songsMigrated > 0) {
                    hatchet.v("Migrated $songsMigrated songs.")
                    settingsManager.setNeedsAutoMigrate(false)
                }
            }
            .flowOn(dispatchers.disk)
            .launchIn(coroutineScope)
    }

    private fun processSongs(songs: List<Song>): Int {
        var playsAdded = 0
        songs.forEach { song ->
            val playCount = song.playCount

            repeat(playCount) {
                val time = System.currentTimeMillis()
                songHistoryRepository.recordSongPlay(song, time)
            }

            playsAdded += playCount
        }

        return playsAdded
    }
}
