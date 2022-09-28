package com.vgleadsheets.di

import com.vgleadsheets.network.MockVglsApi
import com.vgleadsheets.network.StringGenerator
import com.vgleadsheets.network.VglsApi
import dagger.Provides
import java.util.Random
import javax.inject.Named
import javax.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
class ApiModule {
    @Provides
    @Singleton
        fun provideVglsApi(
        @Named("VglsApiUrl") baseUrl: String?,
        @Named("VglsOkHttp") client: OkHttpClient,
        converterFactory: MoshiConverterFactory,
        random: Random,
        @Named("RngSeed") seed: Long,
        stringGenerator: StringGenerator
    ) = if (baseUrl != null) {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(converterFactory)
            .build()
            .create(VglsApi::class.java)
    } else {
        MockVglsApi(random, seed, stringGenerator)
    }
}
