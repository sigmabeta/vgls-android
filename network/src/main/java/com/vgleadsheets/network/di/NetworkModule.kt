package com.vgleadsheets.network.di

import com.vgleadsheets.network.BuildConfig
import com.vgleadsheets.network.VglsApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Named("BaseUrl")
    @Singleton
    internal fun provideBaseUrl() = "https://www.vgleadsheets.com/api/"

    @Provides
    @Singleton
    internal fun provideOkClient(): OkHttpClient {
        return if (BuildConfig.DEBUG) {
            val logger = HttpLoggingInterceptor()

            logger.level = HttpLoggingInterceptor.Level.BODY

            OkHttpClient.Builder()
                .addInterceptor(logger)
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
        @Named("BaseUrl") baseUrl: String,
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