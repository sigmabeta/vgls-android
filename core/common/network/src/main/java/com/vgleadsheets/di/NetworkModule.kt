package com.vgleadsheets.di

import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.vgleadsheets.common.debug.NetworkEndpoint
import com.vgleadsheets.network.BuildConfig
import com.vgleadsheets.storage.Storage
import dagger.Provides
import java.io.File
import java.util.Random
import javax.inject.Named
import javax.inject.Singleton
import kotlinx.coroutines.runBlocking
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
class NetworkModule {
    @Provides
    @Singleton
    @Named("RngSeed")
    internal fun provideSeed() = SEED_RANDOM_NUMBER_GENERATOR

    @Provides
    @Singleton
    internal fun provideRandom(@Named("RngSeed") seed: Long) = Random(seed)

    @Provides
    @Named("NetworkEndpoint")
    @Singleton
    internal fun provideNetworkSetting(storage: Storage): Int {
        return runBlocking {
            storage.getDebugSettingNetworkEndpoint().selectedPosition
        }
    }

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
        @Named("HttpLoggingInterceptor") logger: Interceptor,
        @Named("StethoInterceptor") debugger: Interceptor
    ) = if (BuildConfig.DEBUG) {
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
        context: Context,
        @Named("HttpLoggingInterceptor") logger: Interceptor,
        @Named("StethoInterceptor") debugger: Interceptor,
        @Named("CacheInterceptor") cacher: Interceptor
    ) = if (BuildConfig.DEBUG) {
        OkHttpClient.Builder()
            .cache(Cache(File(context.cacheDir.absolutePath, "okhttp"), CACHE_SIZE_BYTES))
            .addNetworkInterceptor(logger)
            .addNetworkInterceptor(debugger)
            .addNetworkInterceptor(cacher)
            .build()
    } else {
        OkHttpClient()
    }

    @Provides
    @Named("StethoInterceptor")
    internal fun provideStethoInterceptor(): Interceptor = StethoInterceptor()

    @Provides
    @Named("HttpLoggingInterceptor")
    internal fun provideHttpLoggingInterceptor(): Interceptor {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BODY
        return logger
    }

    @Provides
    @Named("CacheInterceptor")
    internal fun provideCacheInterceptor() = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalResponse = chain.proceed(chain.request())
            return originalResponse.newBuilder()
                .header("Cache-Control", "max-age=" + CACHE_MAX_AGE)
                .build()
        }
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
