package com.vgleadsheets.repository

import com.vgleadsheets.model.ApiDigest
import com.vgleadsheets.model.composer.Composer
import com.vgleadsheets.model.game.Game
import com.vgleadsheets.model.jam.ApiJam
import com.vgleadsheets.model.jam.Jam
import com.vgleadsheets.model.jam.SetlistEntry
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.model.tag.TagKey
import com.vgleadsheets.model.tag.TagValue
import com.vgleadsheets.model.time.Time
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface Repository {
    fun checkShouldAutoUpdate(): Single<Boolean>
    fun refresh(): Single<ApiDigest>

    fun refreshJamStateContinuously(name: String): Observable<ApiJam>
    fun refreshJamState(name: String): Single<ApiJam>
    fun refreshSetlist(jamId: Long, name: String): Single<List<Long>>
    fun observeJamState(id: Long): Observable<Jam>

    // Full Lists
    fun getGames(withSongs: Boolean = true): Observable<List<Game>>
    fun getAllSongs(withComposers: Boolean = true): Observable<List<Song>>
    fun getComposers(withSongs: Boolean = true): Observable<List<Composer>>
    fun getAllTagKeys(withValues: Boolean = true): Observable<List<TagKey>>
    fun getJams(): Observable<List<Jam>>

    // Filtered lists
    fun getTagValuesForTagKey(tagKeyId: Long, withSongs: Boolean = true): Observable<List<TagValue>>
    fun getSongsByComposer(composerId: Long): Observable<List<Song>>
    fun getSongsForTagValue(tagValueId: Long): Observable<List<Song>>
    fun getTagValuesForSong(songId: Long): Observable<List<TagValue>>
    fun getSetlistForJam(jamId: Long): Observable<List<SetlistEntry>>
    fun getSongsForGame(
        gameId: Long,
        withComposers: Boolean = true
    ): Observable<List<Song>>

    // Single items
    fun getSong(songId: Long, withComposers: Boolean = true): Observable<Song>
    fun getComposer(composerId: Long): Observable<Composer>
    fun getGame(gameId: Long): Observable<Game>
    fun getTagKey(tagKeyId: Long): Observable<TagKey>
    fun getTagValue(tagValueId: Long): Observable<TagValue>
    fun getLastUpdateTime(): Observable<Time>
    fun getJam(id: Long, withHistory: Boolean): Observable<Jam>

    // Etc
    fun searchSongs(searchQuery: String): Observable<List<Song>>
    fun searchGamesCombined(searchQuery: String): Observable<List<Game>>
    fun searchComposersCombined(searchQuery: String): Observable<List<Composer>>

    // Jam maintenance
    fun removeJam(id: Long): Completable

    // Debug options
    fun clearSheets(): Completable
    fun clearJams(): Completable
}
