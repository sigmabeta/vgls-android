package com.vgleadsheets.repository

import com.vgleadsheets.model.game.Game
import com.vgleadsheets.network.VglsApi
import io.reactivex.Observable
import javax.inject.Inject

class RealRepository @Inject constructor(val vglsApi: VglsApi) : Repository {
    override fun getGames(): Observable<List<Game>> {
        return vglsApi.games().map { list ->
            list.map { apiGame ->
                apiGame.toGame()
            }
        }
    }
}