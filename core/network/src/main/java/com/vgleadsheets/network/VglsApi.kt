package com.vgleadsheets.network

import com.vgleadsheets.model.ApiDigest
import com.vgleadsheets.model.jam.ApiJam
import com.vgleadsheets.model.jam.ApiSetlist
import com.vgleadsheets.model.time.ApiTime
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
