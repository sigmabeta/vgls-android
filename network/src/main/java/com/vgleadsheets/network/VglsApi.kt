package com.vgleadsheets.network

import com.vgleadsheets.model.game.ApiGame
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path


interface VglsApi {
    @GET("games")
    fun games(): Observable<List<ApiGame>>
}