package com.vgleadsheets.di.network

import com.vgleadsheets.network.FakeModelGenerator
import com.vgleadsheets.network.FakeSheetDownloadApi
import com.vgleadsheets.network.FakeVglsApi
import com.vgleadsheets.network.SheetDownloadApi
import com.vgleadsheets.network.VglsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ApiModule {
    @Provides
    @Singleton
    fun provideVglsApi(
        @Named("VglsApiUrl") baseUrl: String?,
        @Named("VglsOkHttp") client: OkHttpClient,
        converterFactory: Converter.Factory,
        fakeModelGenerator: FakeModelGenerator
    ): VglsApi = if (baseUrl != null) {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(converterFactory)
            .build()
            .create(VglsApi::class.java)
    } else {
        FakeVglsApi(fakeModelGenerator)
    }

    @Provides
    @Singleton
    fun provideSheetDownloadApi(
        @Named("VglsPdfUrl") baseUrl: String?,
        @Named("VglsOkHttp") client: OkHttpClient,
    ): SheetDownloadApi = if (baseUrl != null) {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .build().create(SheetDownloadApi::class.java)
    } else {
        FakeSheetDownloadApi()
    }
}
