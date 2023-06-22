package com.vgleadsheets.repository

import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.alias.SongAlias
import com.vgleadsheets.model.tag.TagKey
import com.vgleadsheets.model.tag.TagValue
import com.vgleadsheets.model.time.Time
import kotlinx.coroutines.flow.Flow

interface VglsRepository {
    suspend fun checkShouldAutoUpdate(): Boolean
    fun refresh(): Flow<Unit>

    // Full Lists
    fun getAllGames(): Flow<List<Game>>
    fun getAllSongs(): Flow<List<Song>>
    fun getAllComposers(): Flow<List<Composer>>
    fun getAllTagKeys(): Flow<List<TagKey>>

    // Favorites
    fun getFavoriteGames(): Flow<List<Game>>

    fun getFavoriteSongs(): Flow<List<Song>>

    fun getFavoriteComposers(): Flow<List<Composer>>

    // Related Lists
    fun getSongsForGame(gameId: Long): Flow<List<Song>>
    fun getSongsForGameSync(gameId: Long): List<Song>
    fun getSongsForTagValue(tagValueId: Long): Flow<List<Song>>
    fun getTagValuesForTagKey(tagKeyId: Long): Flow<List<TagValue>>
    fun getTagValuesForSong(songId: Long): Flow<List<TagValue>>
    fun getAliasesForSong(songId: Long): Flow<List<SongAlias>>

    // Single items
    fun getSong(songId: Long): Flow<Song>
    fun getComposer(composerId: Long): Flow<Composer>
    fun getGame(gameId: Long): Flow<Game>
    fun getTagKey(tagKeyId: Long): Flow<TagKey>
    fun getTagValue(tagValueId: Long): Flow<TagValue>
    fun getLastUpdateTime(): Flow<Time>

    // Etc
    fun searchSongsCombined(searchQuery: String): Flow<List<Song>>
    fun searchGamesCombined(searchQuery: String): Flow<List<Game>>
    fun searchComposersCombined(searchQuery: String): Flow<List<Composer>>

    // User data
    suspend fun incrementViewCounter(songId: Long)
    suspend fun toggleFavoriteSong(songId: Long)
    suspend fun toggleFavoriteGame(gameId: Long)
    suspend fun toggleFavoriteComposer(composerId: Long)
    suspend fun toggleOfflineSong(songId: Long)
    suspend fun toggleOfflineGame(gameId: Long)
    suspend fun toggleOfflineComposer(composerId: Long)

    suspend fun toggleAlternate(songId: Long)

    // Debug options
    suspend fun clearSheets()
}
