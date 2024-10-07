package com.vgleadsheets.repository

import com.vgleadsheets.database.dao.ComposerDataSource
import com.vgleadsheets.database.dao.DataSource
import com.vgleadsheets.database.dao.GameDataSource
import com.vgleadsheets.database.dao.SongDataSource
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import java.util.Random

class RandomRepository(
    private val songDataSource: SongDataSource,
    private val composerDataSource: ComposerDataSource,
    private val gameDataSource: GameDataSource,
    private val hatchet: Hatchet,
) {
    private val random = Random()

    fun getRandomSong(seed: Long? = null) = songDataSource
        .getHighestId()
        .map { limit -> randomSongHelper(limit, seed) }
        .take(1)

    fun getRandomSongs(count: Int, seed: Long? = null) = songDataSource
        .getHighestId()
        .map { limit ->
            if (limit <= 0) {
                return@map emptyList()
            }

            List(count) { index ->
                randomSongHelper(limit, seed?.plus(index.toLong()))
            }.distinctBy { it.id }
        }

    fun getRandomComposer(seed: Long? = null) = composerDataSource
        .getHighestId()
        .map { limit ->
            var randomComposer: Composer? = null
            while (randomComposer == null) {
                val randomId = getRng(seed)
                    .nextInt(limit.toInt())
                    .toLong()

                randomComposer = composerDataSource.getItem(TYPE_COMPOSER, randomId)
            }
            randomComposer
        }
        .take(1)

    fun getRandomGame(seed: Long? = null) = gameDataSource
        .getHighestId()
        .map { limit ->
            var randomGame: Game? = null
            while (randomGame == null) {
                val randomId = getRng(seed)
                    .nextInt(limit.toInt())
                    .toLong()

                randomGame = gameDataSource.getItem(TYPE_GAME, randomId)
            }
            randomGame
        }
        .take(1)

    private suspend fun randomSongHelper(limit: Long, seed: Long? = null): Song {
        var randomSong: Song? = null
        while (randomSong == null) {
            val randomId = getRng(seed)
                .nextInt(limit.toInt())
                .toLong()

            randomSong = songDataSource.getItem(TYPE_SONG, randomId)
        }
        return randomSong
    }

    private suspend fun <ModelType> DataSource<ModelType>.getItem(type: String, id: Long) = getOneById(id)
        .catch { ex -> hatchet.w("Error retrieving random $type with id $id; ${ex.message}") }
        .firstOrNull()

    private fun getRng(seed: Long?) = if (seed != null) {
        Random(seed)
    } else {
        random
    }

    companion object {
        private const val TYPE_SONG = "song"
        private const val TYPE_GAME = "game"
        private const val TYPE_COMPOSER = "composer"
    }
}
