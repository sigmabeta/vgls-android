package com.vgleadsheets.repository.history

import com.vgleadsheets.database.dao.SongDataSource
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.model.Song
import kotlin.random.Random
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlinx.coroutines.flow.map

class UserContentGenerator(
    private val songHistoryRepository: SongHistoryRepository,
    private val songDataSource: SongDataSource,
    private val hatchet: Hatchet,
) {
    fun generateRandomUserData() = songDataSource
        .getAll()
        .map { processSongs(it) }

    private fun processSongs(songs: List<Song>): Int {
        val randomizer = Random
        val songsToAdd = randomizer.nextInt(190) + 10

        repeat(songsToAdd) {
            val songToAdd = songs.random(randomizer)
            val time = randomizer.generateTime()

            songHistoryRepository.recordSongPlay(songToAdd, time)
        }

        return songsToAdd
    }

    private fun Random.generateTime(): Long {
        val minAge = MINIMUM_AGE_DAYS.toDuration(DurationUnit.DAYS)
        val maxAge = MAXIMUM_AGE_DAYS.toDuration(DurationUnit.DAYS)

        val ageRange = maxAge - minAge
        return System.currentTimeMillis() -
            nextLong(ageRange.inWholeMilliseconds) -
            minAge.inWholeMilliseconds
    }

    companion object {
        private const val MINIMUM_AGE_DAYS = 5
        private const val MAXIMUM_AGE_DAYS = 30
    }
}
