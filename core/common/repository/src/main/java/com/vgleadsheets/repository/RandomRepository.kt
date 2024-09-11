package com.vgleadsheets.repository

import com.vgleadsheets.database.dao.ComposerDataSource
import com.vgleadsheets.database.dao.GameDataSource
import com.vgleadsheets.database.dao.SongDataSource
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlin.random.Random

class RandomRepository(
    private val songDataSource: SongDataSource,
    private val composerDataSource: ComposerDataSource,
    private val gameDataSource: GameDataSource,
) {
    fun getRandomSong() = songDataSource
        .getHighestId()
        .map { limit ->
            var randomSong: Song? = null
            while (randomSong == null) {
                val randomId = Random
                    .nextInt(limit.toInt())
                    .toLong()

                randomSong = songDataSource
                    .getOneById(randomId)
                    .firstOrNull()
            }
            randomSong
        }
        .take(1)

    fun getRandomSongs(count: Int) = songDataSource
        .getHighestId()
        .map { limit ->
            List(count) {
                var randomSong: Song? = null
                while (randomSong == null) {
                    val randomId = Random
                        .nextInt(limit.toInt())
                        .toLong()

                    randomSong = songDataSource
                        .getOneById(randomId)
                        .firstOrNull()
                }
                randomSong
            }
        }
        .take(1)

    fun getRandomComposer() = composerDataSource
        .getHighestId()
        .map { limit ->
            var randomComposer: Composer? = null
            while (randomComposer == null) {
                val randomId = Random
                    .nextInt(limit.toInt())
                    .toLong()

                randomComposer = composerDataSource
                    .getOneById(randomId)
                    .firstOrNull()
            }
            randomComposer
        }
        .take(1)

    fun getRandomGame() = gameDataSource
        .getHighestId()
        .map { limit ->
            var randomGame: Game? = null
            while (randomGame == null) {
                val randomId = Random
                    .nextInt(limit.toInt())
                    .toLong()

                randomGame = gameDataSource
                    .getOneById(randomId)
                    .firstOrNull()
            }
            randomGame
        }
        .take(1)
}
