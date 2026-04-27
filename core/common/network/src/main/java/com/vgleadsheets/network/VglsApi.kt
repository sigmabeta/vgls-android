package com.vgleadsheets.network

import com.vgleadsheets.network.model.ApiDigest
import com.vgleadsheets.network.model.ApiTime
import retrofit2.http.GET

interface VglsApi {
    @GET("digest?v3=true")
    suspend fun getDigest(): ApiDigest

    @GET(LAST_UPDATE_PATH)
    suspend fun getLastUpdateTime(): ApiTime

    companion object {
        const val LAST_UPDATE_PATH = "digest/last-updated?rfc3339=true"
    }
}
