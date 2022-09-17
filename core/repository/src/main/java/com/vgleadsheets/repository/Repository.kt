package com.vgleadsheets.repository

import com.vgleadsheets.model.composer.Composer
import com.vgleadsheets.model.game.Game
import com.vgleadsheets.model.jam.Jam
import com.vgleadsheets.model.jam.SetlistEntry
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.model.tag.TagKey
import com.vgleadsheets.model.tag.TagValue
import com.vgleadsheets.model.time.Time
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun checkShouldAutoUpdate(): Boolean
    suspend fun refresh()

    fun refreshJamStateContinuously(name: String): Flow<Unit>
    suspend fun refreshJamState(name: String)
    suspend fun refreshSetlist(jamId: Long, name: String)
    fun observeJamState(id: Long): Flow<Jam>

    // Full Lists
    fun getGames(withSongs: Boolean = true): Flow<List<Game>>
    fun getAllSongs(withComposers: Boolean = true): Flow<List<Song>>
    fun getComposers(withSongs: Boolean = true): Flow<List<Composer>>
    fun getAllTagKeys(withValues: Boolean = true): Flow<List<TagKey>>
    fun getJams(): Flow<List<Jam>>

    // Filtered lists
    fun getTagValuesForTagKey(tagKeyId: Long, withSongs: Boolean = true): Flow<List<TagValue>>
    fun getSongsByComposer(composerId: Long): Flow<List<Song>>
    fun getSongsForTagValue(tagValueId: Long): Flow<List<Song>>
    fun getTagValuesForSong(songId: Long): Flow<List<TagValue>>
    fun getSetlistForJam(jamId: Long): Flow<List<SetlistEntry>>
    fun getSongsForGame(
        gameId: Long,
        withComposers: Boolean = true
    ): Flow<List<Song>>

    // Single items
    fun getSong(songId: Long, withComposers: Boolean = true): Flow<Song>
    fun getComposer(composerId: Long): Flow<Composer>
    fun getGame(gameId: Long): Flow<Game>
    fun getTagKey(tagKeyId: Long): Flow<TagKey>
    fun getTagValue(tagValueId: Long): Flow<TagValue>
    fun getLastUpdateTime(): Flow<Time>
    fun getJam(id: Long, withHistory: Boolean): Flow<Jam>

    // Etc
    fun searchSongs(searchQuery: String): Flow<List<Song>>
    fun searchGamesCombined(searchQuery: String): Flow<List<Game>>
    fun searchComposersCombined(searchQuery: String): Flow<List<Composer>>

    // Jam maintenance
    suspend fun removeJam(id: Long)

    // Debug options
    suspend fun clearSheets()
    suspend fun clearJams()
}
