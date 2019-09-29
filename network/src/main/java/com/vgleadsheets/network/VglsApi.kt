package com.vgleadsheets.network

import com.vgleadsheets.model.game.ApiGame
import com.vgleadsheets.model.time.ApiTime
import io.reactivex.Single
import retrofit2.http.GET

interface VglsApi {
    @GET("app/digest")
    fun getDigest(): Single<List<ApiGame>>

    @GET("app/digest/last-updated")
    fun getLastUpdateTime(): Single<ApiTime>
}
