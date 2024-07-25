package com.vgleadsheets.repository

import com.vgleadsheets.database.dao.SongAliasDataSource
import com.vgleadsheets.database.dao.SongDataSource
import com.vgleadsheets.model.Song
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class SongRepository(
    private val songDataSource: SongDataSource,
    private val songAliasDataSource: SongAliasDataSource,
) {
    fun getAllSongs() = songDataSource
        .getAll()

    fun getFavoriteSongs() =
        songDataSource.getFavorites()

    fun getSongsForGame(gameId: Long) = songDataSource
        .getSongsForGame(gameId)

    fun getSongsForGameSync(gameId: Long) = songDataSource
        .getSongsForGameSync(gameId)

    fun getSongsForComposer(composerId: Long) = songDataSource
        .getSongsForComposer(composerId)

    fun getSongsForTagValue(tagValueId: Long) = songDataSource
        .getSongsForTagValue(tagValueId)

    fun getSong(songId: Long) = songDataSource.getOneById(songId)

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

    fun searchSongsCombined(searchQuery: String) = combine(
        searchSongs(searchQuery),
        searchSongAliases(searchQuery)
    ) { songs: List<Song>, songAliases: List<Song> ->
        songs + songAliases
    }.map { songs ->
        songs.distinctBy { it.id }
    }

    @Suppress("MaxLineLength")
    private fun searchSongs(searchQuery: String) = songDataSource
        .searchByName("%$searchQuery%") // Percent characters allow characters before and after the query to match.

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
