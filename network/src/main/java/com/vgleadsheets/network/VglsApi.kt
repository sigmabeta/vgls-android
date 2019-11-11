package com.vgleadsheets.network

import com.vgleadsheets.model.game.VglsApiGame
import com.vgleadsheets.model.time.ApiTime
import io.reactivex.Single
import retrofit2.http.GET

interface VglsApi {
    @GET("app/digest?optimized=true")
    fun getDigest(): Single<List<VglsApiGame>>

    @GET("app/digest/last-updated")
    fun getLastUpdateTime(): Single<ApiTime>
}
