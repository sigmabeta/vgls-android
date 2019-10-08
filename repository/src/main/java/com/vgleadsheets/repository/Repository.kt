package com.vgleadsheets.repository

import com.vgleadsheets.model.composer.Composer
import com.vgleadsheets.model.game.Game
import com.vgleadsheets.model.game.VglsApiGame
import com.vgleadsheets.model.search.SearchResult
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.model.time.Time
import io.reactivex.Observable
import io.reactivex.Single

@Suppress("TooManyFunctions")
interface Repository {
    fun checkForUpdate(): Single<List<VglsApiGame>>
    fun forceRefresh(): Single<List<VglsApiGame>>

    // Full Lists
    fun getGames(): Observable<List<Game>>
    fun getAllSongs(): Observable<List<Song>>
    fun getComposers(): Observable<List<Composer>>

    // Filtered lists
    fun getSongsForGame(gameId: Long): Observable<List<Song>>
    fun getSongsByComposer(composerId: Long): Observable<List<Song>>

    // Single items
    fun getSong(songId: Long): Observable<Song>
    fun getComposer(composerId: Long): Observable<Composer>
    fun getGame(gameId: Long): Observable<Game>
    fun getLastUpdateTime(): Observable<Time>

    // Etc
    fun searchGames(searchQuery: String): Observable<List<SearchResult>>
    fun searchSongs(searchQuery: String): Observable<List<SearchResult>>
    fun searchComposers(searchQuery: String): Observable<List<SearchResult>>

    // Giant Bomb searches
    fun searchGiantBombForGame(vglsId: Long, name: String)
}
