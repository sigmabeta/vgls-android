package com.vgleadsheets.repository.history

import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.database.dao.ComposerDataSource
import com.vgleadsheets.database.dao.GameDataSource
import com.vgleadsheets.database.source.ComposerPlayCountDataSource
import com.vgleadsheets.database.source.GamePlayCountDataSource
import com.vgleadsheets.database.source.SongHistoryDataSource
import com.vgleadsheets.database.source.SongPlayCountDataSource
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.history.SongHistoryEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class SongHistoryRepository(
    private val songHistoryDataSource: SongHistoryDataSource,
    private val gamePlayCountDataSource: GamePlayCountDataSource,
    private val composerPlayCountDataSource: ComposerPlayCountDataSource,
    private val songPlayCountDataSource: SongPlayCountDataSource,
    private val gameDataSource: GameDataSource,
    private val composerDataSource: ComposerDataSource,
    private val coroutineScope: CoroutineScope,
    private val dispatchers: VglsDispatchers,
    private val hatchet: Hatchet,
) {
    fun recordSongPlay(song: Song) {
        coroutineScope.launch(dispatchers.disk) {
            val currentTime = System.currentTimeMillis()
            hatchet.v("Recording play for song: ${song.gameName} - ${song.name}")

            songPlayCountDataSource.incrementPlayCount(song.id, currentTime)
            gamePlayCountDataSource.incrementPlayCount(song.gameId, currentTime)
            songHistoryDataSource.insert(
                SongHistoryEntry(
                    songId = song.id,
                    timeMs = System.currentTimeMillis()
                )
            )

            incrementPlayCountForComposersForSong(song.id, currentTime)
        }
    }

    fun getMostPlaysGames() = gamePlayCountDataSource
        .getMostPlays()
        .flatMapConcat { gameCounts ->
            val ids = gameCounts.map { it.id }
            combine(
                flow { emit(gameCounts) },
                gameDataSource.getByIdList(ids)
            ) { gameCountsToZip, games ->
                gameCountsToZip.zip(games)
            }
        }

    private suspend fun incrementPlayCountForComposersForSong(id: Long, currentTime: Long) {
        val composers = composerDataSource.getComposersForSongSync(id)

        composers.forEach {
            composerPlayCountDataSource.incrementPlayCount(it.id, currentTime)
        }
    }
}
