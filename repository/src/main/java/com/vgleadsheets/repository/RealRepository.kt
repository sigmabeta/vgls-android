package com.vgleadsheets.repository

import com.vgleadsheets.model.game.Game
import com.vgleadsheets.network.VglsApi
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class RealRepository : Repository {

    private val vglsApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.vgleadsheets.com/api/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        retrofit.create(VglsApi::class.java)
    }

    override fun getGames(): Observable<List<Game>> {
        return vglsApi.games().map { list ->
            list.map { apiGame ->
                apiGame.toGame()
            }
        }
    }
}