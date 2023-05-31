package com.vgleadsheets.di

import com.vgleadsheets.network.FakeModelGenerator
import com.vgleadsheets.network.FakeVglsApi
import com.vgleadsheets.network.VglsApi
import dagger.Provides
import java.util.*
import javax.inject.Named
import javax.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@dagger.Module
class ApiModule {
    @Provides
    @Singleton
    fun provideVglsApi(
        @Named("VglsApiUrl") baseUrl: String?,
        @Named("VglsOkHttp") client: OkHttpClient,
        converterFactory: MoshiConverterFactory,
        fakeModelGenerator: FakeModelGenerator
    ) = if (baseUrl != null) {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(converterFactory)
            .build()
            .create(VglsApi::class.java)
    } else {
        FakeVglsApi(fakeModelGenerator)
    }
}
