package com.vgleadsheets.repository

import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Jam
import com.vgleadsheets.model.SetlistEntry
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.SongHistoryEntry
import com.vgleadsheets.model.alias.SongAlias
import com.vgleadsheets.model.tag.TagKey
import com.vgleadsheets.model.tag.TagValue
import com.vgleadsheets.model.time.Time
import kotlinx.coroutines.flow.Flow

interface VglsRepository {
    suspend fun checkShouldAutoUpdate(): Boolean
    fun refresh(): Flow<Unit>

    fun refreshJamStateContinuously(name: String): Flow<Unit>
    suspend fun refreshJamState(name: String)
    fun observeJamState(id: Long): Flow<Jam>

    // Full Lists
    fun getAllGames(withSongs: Boolean = true): Flow<List<Game>>
    fun getAllSongs(withComposers: Boolean = true): Flow<List<Song>>
    fun getAllComposers(withSongs: Boolean = true): Flow<List<Composer>>
    fun getAllTagKeys(withValues: Boolean = true): Flow<List<TagKey>>
    fun getAllJams(withHistory: Boolean): Flow<List<Jam>>

    // Related Lists
    fun getSongsForGame(gameId: Long, withComposers: Boolean = true): Flow<List<Song>>
    fun getSongsForTagValue(tagValueId: Long): Flow<List<Song>>
    fun getTagValuesForTagKey(tagKeyId: Long): Flow<List<TagValue>>
    fun getTagValuesForSong(songId: Long): Flow<List<TagValue>>
    fun getSetlistEntriesForJam(jamId: Long): Flow<List<SetlistEntry>>
    fun getSongHistoryForJam(jamId: Long): Flow<List<SongHistoryEntry>>
    fun getAliasesForSong(songId: Long): Flow<List<SongAlias>>

    // Single items
    fun getSong(songId: Long): Flow<Song>
    fun getComposer(composerId: Long): Flow<Composer>
    fun getGame(gameId: Long): Flow<Game>
    fun getTagKey(tagKeyId: Long): Flow<TagKey>
    fun getTagValue(tagValueId: Long): Flow<TagValue>
    fun getJam(id: Long, withHistory: Boolean): Flow<Jam>
    fun getLastUpdateTime(): Flow<Time>

    // Etc
    fun searchSongsCombined(searchQuery: String): Flow<List<Song>>
    fun searchGamesCombined(searchQuery: String): Flow<List<Game>>
    fun searchComposersCombined(searchQuery: String): Flow<List<Composer>>

    // Jam maintenance
    suspend fun refreshJams()
    suspend fun removeJam(id: Long)

    // Debug options
    suspend fun clearSheets()
    suspend fun clearJams()
}
