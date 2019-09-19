package com.vgleadsheets.repository

import com.vgleadsheets.model.composer.Composer
import com.vgleadsheets.model.game.Game
import com.vgleadsheets.model.search.SearchResult
import com.vgleadsheets.model.song.Song
import io.reactivex.Observable

interface Repository {
    fun getGames(force: Boolean = false): Observable<Data<List<Game>>>
    fun getSongs(gameId: Long): Observable<Data<List<Song>>>
    fun getSong(songId: Long): Observable<Data<Song>>
    fun getComposers(): Observable<Data<List<Composer>>>
    fun search(searchQuery: String): Observable<List<SearchResult>>
}
