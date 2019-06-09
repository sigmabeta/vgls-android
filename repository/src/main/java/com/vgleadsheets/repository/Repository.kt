package com.vgleadsheets.repository

import com.vgleadsheets.model.game.Game
import com.vgleadsheets.model.game.GameEntity
import com.vgleadsheets.model.song.Song
import io.reactivex.Observable

interface Repository {
    fun getGames(force: Boolean = false): Observable<Data<List<Game>>>
    fun getSheets(gameId: Long): Observable<Data<List<Song>>>
}
