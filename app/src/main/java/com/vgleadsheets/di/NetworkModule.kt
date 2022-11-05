package com.vgleadsheets.di


import com.vgleadsheets.common.debug.NetworkEndpoint
import dagger.Provides
import java.io.File
import java.util.Random
import javax.inject.Named
import javax.inject.Singleton
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.moshi.MoshiConverterFactory

@dagger.Module
class NetworkModule {
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
        @Named("NetworkEndpoint") selectedNetwork: Int
    ) = NetworkEndpoint.values()[selectedNetwork].url

    @Provides
    @Named("VglsApiUrl")
    @Singleton
    internal fun provideVglsApiUrl(
        @Named("VglsUrl") baseUrl: String?
    ) = if (baseUrl != null) {
        baseUrl + "api/"
    } else {
        null
    }

    @Provides
    @Named("VglsImageUrl")
    @Singleton
    internal fun provideVglsImageUrl(
        @Named("VglsUrl") baseUrl: String?
    ) = if (baseUrl != null) {
        baseUrl + "assets/sheets/png/"
    } else {
        "file:///android_asset/sheets"
    }

    @Provides
    @Singleton
    @Named("VglsOkHttp")
    internal fun provideVglsOkClient(
        @Named("IsDebugBuild") isDebug: Boolean,
        @Named("HttpLoggingInterceptor") logger: Interceptor,
        @Named("StethoInterceptor") debugger: Interceptor,
    ) = if (isDebug) {
        OkHttpClient.Builder()
            .addNetworkInterceptor(logger)
            .addNetworkInterceptor(debugger)
            .build()
    } else {
        OkHttpClient()
    }

    @Provides
    @Singleton
    @Named("PicassoOkHttp")
    internal fun providePicassoOkClient(
        @Named("IsDebugBuild") isDebug: Boolean,
        @Named("CachePath") cachePath: String,
        @Named("HttpLoggingInterceptor") logger: Interceptor,
        @Named("StethoInterceptor") debugger: Interceptor,
        @Named("CacheInterceptor") cacher: Interceptor
    ) = if (isDebug) {
        OkHttpClient.Builder()
            .cache(Cache(File(cachePath, "okhttp"), CACHE_SIZE_BYTES))
            .addNetworkInterceptor(logger)
            .addNetworkInterceptor(debugger)
            .addNetworkInterceptor(cacher)
            .build()
    } else {
        OkHttpClient()
    }

    @Provides
    @Named("HttpLoggingInterceptor")
    internal fun provideHttpLoggingInterceptor(): Interceptor {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BODY
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
    internal fun provideConverterFactory() = MoshiConverterFactory.create()

    companion object {
        const val CACHE_SIZE_BYTES = 100000000L
        const val CACHE_MAX_AGE = 60 * 60 * 24 * 365

        const val SEED_RANDOM_NUMBER_GENERATOR = 123456L
    }
}
