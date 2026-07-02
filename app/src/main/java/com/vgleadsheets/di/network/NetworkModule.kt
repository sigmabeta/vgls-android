package com.vgleadsheets.di.network

import com.squareup.moshi.Moshi
import com.vgleadsheets.di.HatchetOkHttpLogger
import com.vgleadsheets.network.OfflineFailFastInterceptor
import com.vgleadsheets.urlinfo.UrlInfoProvider
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Named
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import net.sigmabeta.sage.appinfo.AppInfo
import net.sigmabeta.sage.di.AppScope
import net.sigmabeta.sage.logging.Hatchet
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.Random

@BindingContainer
@ContributesTo(AppScope::class)
object NetworkModule {
    @Provides
    @SingleIn(AppScope::class)
    @Named("RngSeed")
    fun provideSeed(): Long = SEED_RANDOM_NUMBER_GENERATOR

    @Provides
    @SingleIn(AppScope::class)
    fun provideRandom(@Named("RngSeed") seed: Long): Random = Random(seed)

    @Provides
    @Named("VglsUrl")
    @SingleIn(AppScope::class)
    fun provideVglsUrl(
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
    @SingleIn(AppScope::class)
    fun provideVglsApiUrl(
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
    @SingleIn(AppScope::class)
    fun provideVglsImageUrl(
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
    @SingleIn(AppScope::class)
    fun provideVglsPdfUrl(
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
    @SingleIn(AppScope::class)
    @Named("ProbeOkHttp")
    fun provideProbeOkClient(
        appInfo: AppInfo,
        @Named("HttpLoggingInterceptor") logger: Interceptor,
    ): OkHttpClient = if (appInfo.isDebug) {
        OkHttpClient.Builder()
            .addNetworkInterceptor(logger)
            .build()
    } else {
        OkHttpClient()
    }

    @Provides
    @SingleIn(AppScope::class)
    @Named("VglsOkHttp")
    fun provideVglsOkClient(
        @Named("ProbeOkHttp") base: OkHttpClient,
        failFast: OfflineFailFastInterceptor,
    ): OkHttpClient = base.newBuilder()
        .addInterceptor(failFast)
        .build()

    @Provides
    fun provideHatchetLogger(hatchet: Hatchet): HatchetOkHttpLogger = HatchetOkHttpLogger(hatchet)

    @Provides
    @Named("HttpLoggingInterceptor")
    fun provideHttpLoggingInterceptor(hatchetOkHttpLogger: HatchetOkHttpLogger): Interceptor {
        val logger = HttpLoggingInterceptor(hatchetOkHttpLogger)
        logger.level = HttpLoggingInterceptor.Level.HEADERS
        return logger
    }

    @Provides
    @Named("CacheInterceptor")
    fun provideCacheInterceptor(): Interceptor = Interceptor { chain ->
        val originalResponse = chain.proceed(chain.request())
        originalResponse.newBuilder()
            .header("Cache-Control", "max-age=$CACHE_MAX_AGE")
            .build()
    }

    @Provides
    @SingleIn(AppScope::class)
    fun provideMoshi(): Moshi = Moshi
        .Builder()
        .build()

    @Provides
    @SingleIn(AppScope::class)
    fun provideConverterFactory(
        moshiInstance: Moshi
    ): Converter.Factory = MoshiConverterFactory.create(moshiInstance)

    const val CACHE_MAX_AGE = 60 * 60 * 24 * 365

    const val SEED_RANDOM_NUMBER_GENERATOR = 12345L
}
