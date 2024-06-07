package com.vgleadsheets.network

import com.vgleadsheets.network.model.ApiDigest
import com.vgleadsheets.network.model.ApiTime
import retrofit2.http.GET

interface VglsApi {
    @GET("digest?v3=true")
    suspend fun getDigest(): ApiDigest

    @GET("digest/last-updated?rfc3339=true")
    suspend fun getLastUpdateTime(): ApiTime
}
