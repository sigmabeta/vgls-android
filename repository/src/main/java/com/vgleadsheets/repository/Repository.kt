package com.vgleadsheets.repository

import com.vgleadsheets.model.game.Game
import io.reactivex.Observable

interface Repository {
    fun getGames(): Observable<List<Game>>

    fun close()
}
