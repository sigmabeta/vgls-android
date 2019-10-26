package com.vgleadsheets.repository

import com.vgleadsheets.model.composer.Composer
import com.vgleadsheets.model.game.Game
import com.vgleadsheets.model.game.VglsApiGame
import com.vgleadsheets.model.parts.Part
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.model.time.Time
import io.reactivex.Observable
import io.reactivex.Single

@Suppress("TooManyFunctions")
interface Repository {
    fun checkForUpdate(): Single<List<VglsApiGame>>
    fun forceRefresh(): Single<List<VglsApiGame>>

    // Full Lists
    fun getGames(withSongs: Boolean = true): Observable<List<Game>>
    fun getAllSongs(withComposers: Boolean = true, withParts: Boolean = true): Observable<List<Song>>
    fun getComposers(withSongs: Boolean = true): Observable<List<Composer>>

    // Filtered lists
    fun getPartsForSong(songId: Long, withPages: Boolean = true): Observable<List<Part>>
    fun getSongsByComposer(composerId: Long, withParts: Boolean = true): Observable<List<Song>>
    fun getSongsForGame(
        gameId: Long,
        withParts: Boolean = true,
        withComposers: Boolean = true
    ): Observable<List<Song>>

    // Single items
    fun getSong(songId: Long, withParts: Boolean = true, withComposers: Boolean = true): Observable<Song>
    fun getComposer(composerId: Long): Observable<Composer>
    fun getGame(gameId: Long): Observable<Game>
    fun getLastUpdateTime(): Observable<Time>

    // Etc
    fun searchSongs(searchQuery: String): Observable<List<Song>>
    fun searchGamesCombined(searchQuery: String): Observable<List<Game>>
    fun searchComposersCombined(searchQuery: String): Observable<List<Composer>>

    // Giant Bomb searches
    fun searchGiantBombForGame(vglsId: Long, name: String)
    fun searchGiantBombForComposer(vglsId: Long, name: String)
}
