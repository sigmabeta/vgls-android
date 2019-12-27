package com.vgleadsheets.network

import com.vgleadsheets.model.game.VglsApiGame
import com.vgleadsheets.model.jam.ApiJam
import com.vgleadsheets.model.time.ApiTime
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface VglsApi {
    @GET("app/digest?optimized=true")
    fun getDigest(): Single<List<VglsApiGame>>

    @GET("app/digest/last-updated?rfc3339=true")
    fun getLastUpdateTime(): Single<ApiTime>

    @GET("conductor/{id}?jam_id=true")
    fun getJamState(@Query("id") id: String): Observable<ApiJam>
}
