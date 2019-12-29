package com.vgleadsheets.network.di

import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.vgleadsheets.network.BuildConfig
import com.vgleadsheets.network.GiantBombApi
import com.vgleadsheets.network.GiantBombNoKeyApi
import com.vgleadsheets.network.VglsApi
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import javax.inject.Named
import javax.inject.Singleton

@Suppress("TooManyFunctions")
@Module
class NetworkModule {
    @Provides
    @Named("GiantBombUrl")
    @Singleton
    internal fun provideGiantBombUrl() = "https://www.giantbomb.com/api/"

    @Provides
    @Named("VglsUrl")
    @Singleton
    internal fun provideVglsUrl() = "https://super.vgleadsheets.com/"

    @Provides
    @Named("VglsApiUrl")
    @Singleton
    internal fun provideVglsApiUrl(@Named("VglsUrl") baseUrl: String) = baseUrl + "api/"

    @Provides
    @Named("VglsImageUrl")
    @Singleton
    internal fun provideVglsImageUrl(@Named("VglsUrl") baseUrl: String) =
        baseUrl + "assets/sheets/png/"

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
    @Singleton
    @Named("GiantBombOkHttp")
    internal fun provideGiantBombOkClient(
        @Named("GiantBombApiKeyInterceptor") keyInterceptor: Interceptor,
        @Named("GiantBombJsonInterceptor") formatInterceptor: Interceptor,
        @Named("HttpLoggingInterceptor") logger: Interceptor,
        @Named("StethoInterceptor") debugger: Interceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()

        builder.addNetworkInterceptor(keyInterceptor)
        builder.addNetworkInterceptor(formatInterceptor)

        return if (BuildConfig.DEBUG) {
            builder
                .addNetworkInterceptor(logger)
                .addNetworkInterceptor(debugger)
                .build()
        } else {
            builder.build()
        }
    }

    @Provides
    @Named("GiantBombApiKey")
    internal fun provideGiantBombApiKey() = BuildConfig.GiantBombApiKey

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
    @Named("GiantBombJsonInterceptor")
    internal fun provideGiantBombJsonInterceptor() =
        queryParamInterceptor("format", "json")

    @Provides
    @Named("GiantBombApiKeyInterceptor")
    internal fun provideGiantBombApiKeyInterceptor(@Named("GiantBombApiKey") apiKey: String) =
        queryParamInterceptor("api_key", apiKey)

    private fun queryParamInterceptor(key: String, value: String): Interceptor {
        return object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val original = chain.request()
                val originalHttpUrl = original.url

                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter(key, value)
                    .build()

                // Request customization: add request headers
                val requestBuilder = original.newBuilder()
                    .url(url)

                val request = requestBuilder.build()
                return chain.proceed(request)
            }
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
        @Named("VglsApiUrl") baseUrl: String,
        @Named("VglsOkHttp") client: OkHttpClient,
        converterFactory: MoshiConverterFactory,
        callAdapterFactory: RxJava2CallAdapterFactory
    ) = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addCallAdapterFactory(callAdapterFactory)
        .addConverterFactory(converterFactory)
        .build()
        .create(VglsApi::class.java)

    @Provides
    @Singleton
    fun provideGiantBombApi(
        @Named("GiantBombApiKey") apiKey: String,
        @Named("GiantBombUrl") baseUrl: String,
        @Named("GiantBombOkHttp") client: OkHttpClient,
        converterFactory: MoshiConverterFactory,
        callAdapterFactory: RxJava2CallAdapterFactory
    ) = if (apiKey == "empty") {
        GiantBombNoKeyApi()
    } else {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addCallAdapterFactory(callAdapterFactory)
            .addConverterFactory(converterFactory)
            .build()
            .create(GiantBombApi::class.java)
    }

    companion object {
        const val CACHE_SIZE_BYTES = 100000000L
        const val CACHE_MAX_AGE = 60 * 60 * 24 * 365
    }
}
