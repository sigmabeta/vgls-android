package com.vgleadsheets.repository

import com.vgleadsheets.database.dao.SongAliasDataSource
import com.vgleadsheets.database.dao.SongDataSource
import com.vgleadsheets.database.source.AlternateSettingDataSource

class SongRepository(
    private val songDataSource: SongDataSource,
    private val songAliasDataSource: SongAliasDataSource,
    private val alternateSettingDataSource: AlternateSettingDataSource,
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

    suspend fun toggleAlternate(songId: Long) {
        val areFavoritesMigratedYet = false

        alternateSettingDataSource.toggleAlternate(songId)
    }

    fun getAliasesForSong(songId: Long) = songAliasDataSource
        .getAliasesForSong(songId)

    fun isAlternateSelected(id: Long) = alternateSettingDataSource.isAlternateSelected(id)
}
