package com.vgleadsheets.network

import com.vgleadsheets.model.giantbomb.GiantBombGame
import com.vgleadsheets.model.giantbomb.GiantBombPerson
import com.vgleadsheets.model.giantbomb.GiantBombSearchResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GiantBombApi {
    @GET("search/")
    fun searchForGame(
        @Query("query") name: String,
        @Query("resources") resources: String = "game",
        @Query("field_list") field_list: String = "name,aliases,image"
    ): Single<GiantBombSearchResponse<GiantBombGame>>

    @GET("search/")
    fun searchForComposer(
        @Query("query") name: String,
        @Query("resources") resources: String = "person",
        @Query("field_list") field_list: String = "name,aliases,image"
    ): Single<GiantBombSearchResponse<GiantBombPerson>>
}
