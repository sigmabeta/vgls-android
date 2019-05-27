package com.vgleadsheets.repository

import com.vgleadsheets.model.game.GameEntity
import io.reactivex.Observable

interface Repository {
    fun getGames(force: Boolean = false): Observable<Data<List<GameEntity>>>
}
