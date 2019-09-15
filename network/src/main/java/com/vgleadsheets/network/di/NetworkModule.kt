package com.vgleadsheets.network.di

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.vgleadsheets.network.BuildConfig
import com.vgleadsheets.network.VglsApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {
    @Provides
    @Named("BaseUrl")
    @Singleton
    internal fun provideBaseUrl() = "https://super.vgleadsheets.com/"

    @Provides
    @Named("BaseApiUrl")
    @Singleton
    internal fun provideBaseApiUrl(@Named("BaseUrl") baseUrl: String) = baseUrl + "api/"


    @Provides
    @Named("BaseImageUrl")
    @Singleton
    internal fun provideBaseImageUrl(@Named("BaseUrl") baseUrl: String) = baseUrl + "assets/sheets/png/"

    @Provides
    @Singleton
    internal fun provideOkClient(): OkHttpClient {
        return if (BuildConfig.DEBUG) {
            val debugger = StethoInterceptor()
            val logger = HttpLoggingInterceptor()

            logger.level = HttpLoggingInterceptor.Level.BODY
            OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor(debugger)
                .build()
        } else {
            OkHttpClient()
        }
    }

    @Provides
    @Singleton
    internal fun provideCallAdapterFactory() = RxJava2CallAdapterFactory.createAsync()

    @Provides
    @Singleton
    internal fun provideConverterFactory() = MoshiConverterFactory.create()

    @Provides
    @Singleton
    fun provideVglsApi(
        @Named("BaseApiUrl") baseUrl: String,
        client: OkHttpClient,
        converterFactory: MoshiConverterFactory,
        callAdapterFactory: RxJava2CallAdapterFactory
    ) = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addCallAdapterFactory(callAdapterFactory)
        .addConverterFactory(converterFactory)
        .build()
        .create(VglsApi::class.java)
}
