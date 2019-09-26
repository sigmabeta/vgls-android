package com.vgleadsheets.repository

import com.vgleadsheets.model.composer.Composer
import com.vgleadsheets.model.game.Game
import com.vgleadsheets.model.search.SearchResult
import com.vgleadsheets.model.song.Song
import io.reactivex.Observable

interface Repository {
    // Full Lists
    fun getGames(force: Boolean = false): Observable<Data<List<Game>>>
    fun getAllSongs(): Observable<Data<List<Song>>>
    fun getComposers(): Observable<Data<List<Composer>>>

    // Filtered lists
    fun getSongsForGame(gameId: Long): Observable<Data<List<Song>>>
    fun getSongsByComposer(composerId: Long): Observable<Data<List<Song>>>

    // Single items
    fun getSong(songId: Long): Observable<Data<Song>>
    fun getComposer(composerId: Long): Observable<Composer>
    fun getGame(gameId: Long): Observable<Game>

    // Etc
    fun search(searchQuery: String): Observable<List<SearchResult>>
}
