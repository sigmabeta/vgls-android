package com.vgleadsheets.network

import com.vgleadsheets.model.game.ApiGame
import io.reactivex.Observable
import retrofit2.http.GET

interface VglsApi {
    @GET("games")
    fun getAllGames(): Observable<List<ApiGame>>
}