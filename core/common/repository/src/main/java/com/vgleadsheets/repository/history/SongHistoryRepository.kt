package com.vgleadsheets.repository.history

import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.database.dao.ComposerDataSource
import com.vgleadsheets.database.source.ComposerPlayCountDataSource
import com.vgleadsheets.database.source.GamePlayCountDataSource
import com.vgleadsheets.database.source.SongHistoryDataSource
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.history.SongHistoryEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SongHistoryRepository(
    private val songHistoryDataSource: SongHistoryDataSource,
    private val gamePlayCountDataSource: GamePlayCountDataSource,
    private val composerPlayCountDataSource: ComposerPlayCountDataSource,
    private val composerDataSource: ComposerDataSource,
    private val coroutineScope: CoroutineScope,
    private val dispatchers: VglsDispatchers,
    private val hatchet: Hatchet,
) {
    fun recordSongPlay(song: Song) {
        coroutineScope.launch(dispatchers.disk) {
            hatchet.v("Recording play for song: ${song.gameName} - ${song.name}")
            gamePlayCountDataSource.incrementPlayCount(song.gameId)
            songHistoryDataSource.insert(
                SongHistoryEntry(
                    songId = song.id,
                    timeMs = System.currentTimeMillis()
                )
            )

            incrementPlayCountForComposersForSong(song.id)
        }
    }

    private suspend fun incrementPlayCountForComposersForSong(id: Long) {
        val composers = composerDataSource.getComposersForSongSync(id)

        composers.forEach {
            composerPlayCountDataSource.incrementPlayCount(it.id)
        }
    }
}
