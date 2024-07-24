package com.vgleadsheets.repository

import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.database.dao.SongAliasDataSource
import com.vgleadsheets.database.dao.SongDataSource
import com.vgleadsheets.model.Song
import kotlin.random.Random
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take

class SongRepository(
    private val dispatchers: VglsDispatchers,
    private val songDataSource: SongDataSource,
    private val songAliasDataSource: SongAliasDataSource,
) {
    fun getAllSongs() = songDataSource
        .getAll()
        .flowOn(dispatchers.disk)

    fun getFavoriteSongs() =
        songDataSource.getFavorites().flowOn(dispatchers.disk)

    fun getSongsForGame(gameId: Long) = songDataSource
        .getSongsForGame(gameId)
        .flowOn(dispatchers.disk)

    fun getSongsForGameSync(gameId: Long) = songDataSource
        .getSongsForGameSync(gameId)

    fun getSongsForComposer(composerId: Long) = songDataSource
        .getSongsForComposer(composerId)
        .flowOn(dispatchers.disk)

    fun getSongsForTagValue(tagValueId: Long) = songDataSource
        .getSongsForTagValue(tagValueId)
        .flowOn(dispatchers.disk)

    fun getSong(
        songId: Long
    ) = songDataSource
        .getOneById(songId)
        .flowOn(dispatchers.disk)

    suspend fun toggleFavoriteSong(songId: Long) {
        songDataSource.toggleFavorite(songId)
    }

    suspend fun toggleOfflineSong(songId: Long) {
        songDataSource.toggleOffline(songId)
    }

    suspend fun toggleAlternate(songId: Long) {
        songDataSource.toggleAlternate(songId)
    }

    fun getAliasesForSong(songId: Long) = songAliasDataSource
        .getAliasesForSong(songId)
        .flowOn(dispatchers.disk)

    fun searchSongsCombined(searchQuery: String) = combine(
        searchSongs(searchQuery),
        searchSongAliases(searchQuery)
    ) { songs: List<Song>, songAliases: List<Song> ->
        songs + songAliases
    }.map { songs ->
        songs.distinctBy { it.id }
    }.flowOn(dispatchers.disk)

    fun getRandomSong() = getHighestId()
        .map { limit ->
            var randomSong: Song? = null
            while (randomSong == null) {
                val randomId = Random.nextInt(limit.toInt())
                randomSong = getSong(randomId.toLong()).firstOrNull()
            }
            randomSong
        }
        .take(1)

    private fun getHighestId() = songDataSource.getHighestId()

    @Suppress("MaxLineLength")
    private fun searchSongs(searchQuery: String) = songDataSource
        .searchByName("%$searchQuery%") // Percent characters allow characters before and after the query to match.
        .flowOn(dispatchers.disk)

    @Suppress("MaxLineLength")
    private fun searchSongAliases(searchQuery: String) = songAliasDataSource
        .searchByName("%$searchQuery%") // Percent characters allow characters before and after the query to match.
        .map { list ->
            list.mapNotNull {
                it.song?.copy(
                    name = it.name,
                )
            }
        }
}
