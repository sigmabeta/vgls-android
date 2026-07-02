package com.vgleadsheets.di.network

import com.vgleadsheets.network.FakeModelGenerator
import com.vgleadsheets.network.FakeSheetDownloadApi
import com.vgleadsheets.network.FakeVglsApi
import com.vgleadsheets.network.SheetDownloadApi
import com.vgleadsheets.network.VglsApi
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Named
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import net.sigmabeta.sage.di.AppScope
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit

@BindingContainer
@ContributesTo(AppScope::class)
object ApiModule {
    @Provides
    @SingleIn(AppScope::class)
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
    @SingleIn(AppScope::class)
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
