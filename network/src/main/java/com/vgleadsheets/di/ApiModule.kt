package com.vgleadsheets.di

import com.vgleadsheets.network.GiantBombApi
import com.vgleadsheets.network.GiantBombNoKeyApi
import com.vgleadsheets.network.MockGiantBombApi
import com.vgleadsheets.network.MockVglsApi
import com.vgleadsheets.network.StringGenerator
import com.vgleadsheets.network.VglsApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.Random
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApiModule {
    @Provides
    @Singleton
    @Suppress("LongParameterList")
    fun provideVglsApi(
        @Named("VglsApiUrl") baseUrl: String?,
        @Named("VglsOkHttp") client: OkHttpClient,
        converterFactory: MoshiConverterFactory,
        callAdapterFactory: RxJava2CallAdapterFactory,
        random: Random,
        @Named("RngSeed") seed: Long,
        stringGenerator: StringGenerator
    ) = if (baseUrl != null) {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addCallAdapterFactory(callAdapterFactory)
            .addConverterFactory(converterFactory)
            .build()
            .create(VglsApi::class.java)
    } else {
        MockVglsApi(random, seed, stringGenerator)
    }

    @Provides
    @Singleton
    @Suppress("LongParameterList")
    fun provideGiantBombApi(
        @Named("GiantBombApiKey") apiKey: String,
        @Named("GiantBombUrl") baseUrl: String?,
        @Named("GiantBombOkHttp") client: OkHttpClient,
        converterFactory: MoshiConverterFactory,
        callAdapterFactory: RxJava2CallAdapterFactory,
        random: Random
    ) = if (baseUrl == null) {
        MockGiantBombApi(random)
    } else if (apiKey == "empty") {
        GiantBombNoKeyApi()
    } else {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addCallAdapterFactory(callAdapterFactory)
            .addConverterFactory(converterFactory)
            .build()
            .create(GiantBombApi::class.java)
    }
}
