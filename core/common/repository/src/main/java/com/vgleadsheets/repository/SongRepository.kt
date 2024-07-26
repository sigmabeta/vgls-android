package com.vgleadsheets.repository

import com.vgleadsheets.database.dao.SongAliasDataSource
import com.vgleadsheets.database.dao.SongDataSource

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
}
