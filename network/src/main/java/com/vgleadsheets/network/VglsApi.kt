package com.vgleadsheets.network

import com.vgleadsheets.model.ApiDigest
import com.vgleadsheets.model.jam.ApiJam
import com.vgleadsheets.model.jam.ApiSetlist
import com.vgleadsheets.model.time.ApiTime
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface VglsApi {
    @GET("app/digest?v3=true")
    fun getDigest(): Single<ApiDigest>

    @GET("app/digest/last-updated?rfc3339=true")
    fun getLastUpdateTime(): Single<ApiTime>

    @GET("conductor/{name}?jam_id=true")
    fun getJamState(@Path("name") name: String): Observable<ApiJam>

    @GET("conductor/{name}/setlist")
    fun getSetlistForJam(@Path("name") name: String): Single<ApiSetlist>
}
