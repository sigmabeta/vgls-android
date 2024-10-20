package com.vgleadsheets.di.network

import com.squareup.moshi.Moshi
import com.vgleadsheets.appinfo.AppInfo
import com.vgleadsheets.di.HatchetOkHttpLogger
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.urlinfo.UrlInfoProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.Random
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    @Provides
    @Singleton
    @Named("RngSeed")
    internal fun provideSeed() = SEED_RANDOM_NUMBER_GENERATOR

    @Provides
    @Singleton
    internal fun provideRandom(@Named("RngSeed") seed: Long) = Random(seed)

    @Provides
    @Named("VglsUrl")
    @Singleton
    internal fun provideVglsUrl(
        urlInfoProvider: UrlInfoProvider,
    ): String? {
        return runBlocking {
            val urlInfo = urlInfoProvider
                .urlInfoFlow
                .first { it.loaded }

            return@runBlocking urlInfo.baseBaseUrl
        }
    }

    @Provides
    @Named("VglsApiUrl")
    @Singleton
    internal fun provideVglsApiUrl(
        urlInfoProvider: UrlInfoProvider,
    ): String? {
        return runBlocking {
            val urlInfo = urlInfoProvider
                .urlInfoFlow
                .first { it.loaded }

            return@runBlocking urlInfo.apiBaseUrl
        }
    }

    @Provides
    @Named("VglsImageUrl")
    @Singleton
    internal fun provideVglsImageUrl(
        urlInfoProvider: UrlInfoProvider,
    ): String? {
        return runBlocking {
            val urlInfo = urlInfoProvider
                .urlInfoFlow
                .first { it.loaded }

            return@runBlocking urlInfo.imageBaseUrl
        }
    }

    @Provides
    @Named("VglsPdfUrl")
    @Singleton
    internal fun provideVglsPdfUrl(
        urlInfoProvider: UrlInfoProvider,
    ): String? {
        val baseUrl = runBlocking {
            val urlInfo = urlInfoProvider
                .urlInfoFlow
                .first { it.loaded }

            return@runBlocking urlInfo.pdfBaseUrl
        }

        return baseUrl
    }

    @Provides
    @Singleton
    @Named("VglsOkHttp")
    internal fun provideVglsOkClient(
        appInfo: AppInfo,
        @Named("HttpLoggingInterceptor") logger: Interceptor,
    ) = if (appInfo.isDebug) {
        OkHttpClient.Builder()
            .addNetworkInterceptor(logger)
            .build()
    } else {
        OkHttpClient()
    }

    @Provides
    fun provideHatchetLogger(hatchet: Hatchet) = HatchetOkHttpLogger(hatchet)

    @Provides
    @Named("HttpLoggingInterceptor")
    internal fun provideHttpLoggingInterceptor(hatchetOkHttpLogger: HatchetOkHttpLogger): Interceptor {
        val logger = HttpLoggingInterceptor(hatchetOkHttpLogger)
        logger.level = HttpLoggingInterceptor.Level.HEADERS
        return logger
    }

    @Provides
    @Named("CacheInterceptor")
    internal fun provideCacheInterceptor() = Interceptor { chain ->
        val originalResponse = chain.proceed(chain.request())
        originalResponse.newBuilder()
            .header("Cache-Control", "max-age=$CACHE_MAX_AGE")
            .build()
    }

    @Provides
    @Singleton
    internal fun provideMoshi(): Moshi = Moshi
        .Builder()
        .build()

    @Provides
    @Singleton
    internal fun provideConverterFactory(
        moshiInstance: Moshi
    ): Converter.Factory = MoshiConverterFactory.create(moshiInstance)

    const val CACHE_MAX_AGE = 60 * 60 * 24 * 365

    const val SEED_RANDOM_NUMBER_GENERATOR = 12345L
}
