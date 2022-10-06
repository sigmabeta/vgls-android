package com.vgleadsheets.network

import com.vgleadsheets.network.model.ApiDigest
import com.vgleadsheets.network.model.ApiJam
import com.vgleadsheets.network.model.ApiSetlist
import com.vgleadsheets.network.model.ApiTime
import retrofit2.http.GET
import retrofit2.http.Path

interface VglsApi {
    @GET("app/digest?v3=true")
    suspend fun getDigest(): ApiDigest

    @GET("app/digest/last-updated?rfc3339=true")
    suspend fun getLastUpdateTime(): ApiTime

    @GET("conductor/{name}?jam_id=true")
    suspend fun getJamState(@Path("name") name: String): ApiJam

    @GET("conductor/{name}/setlist")
    suspend fun getSetlistForJam(@Path("name") name: String): ApiSetlist
}
