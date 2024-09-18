package com.vgleadsheets.repository.history

import com.vgleadsheets.database.dao.SongDataSource
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.model.Song
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take

class UserContentMigrator(
    private val songHistoryRepository: SongHistoryRepository,
    private val songDataSource: SongDataSource,
    private val hatchet: Hatchet,
) {
    fun migrateUserData() = songDataSource
        .getAll()
        .filter { it.isNotEmpty() }
        .take(1)
        .map { processSongs(it) }

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
