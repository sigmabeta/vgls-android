package com.vgleadsheets.network.di

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.vgleadsheets.network.BuildConfig
import com.vgleadsheets.network.GiantBombApi
import com.vgleadsheets.network.VglsApi
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

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
    internal fun provideVglsOkClient() = if (BuildConfig.DEBUG) {
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

    @Provides
    @Singleton
    @Named("GiantBombOkHttp")
    internal fun provideGiantBombOkClient(
        @Named("GiantBombApiKeyInterceptor") keyInterceptor: Interceptor,
        @Named("GiantBombJsonInterceptor") formatInterceptor: Interceptor
    ) : OkHttpClient {
        val builder = OkHttpClient.Builder()

        builder.addInterceptor(keyInterceptor)
        builder.addInterceptor(formatInterceptor)

        return if (BuildConfig.DEBUG) {
            val debugger = StethoInterceptor()
            val logger = HttpLoggingInterceptor()

            logger.level = HttpLoggingInterceptor.Level.BODY
            builder
                .addInterceptor(logger)
                .addInterceptor(debugger)
                .build()
        } else {
            builder.build()
        }
    }


    @Provides
    @Named("GiantBombApiKey")
    internal fun provideGiantBombApiKey() = BuildConfig.GiantBombApiKey

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
        @Named("GiantBombUrl") baseUrl: String,
        @Named("GiantBombOkHttp") client: OkHttpClient,
        converterFactory: MoshiConverterFactory,
        callAdapterFactory: RxJava2CallAdapterFactory
    ) = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addCallAdapterFactory(callAdapterFactory)
        .addConverterFactory(converterFactory)
        .build()
        .create(GiantBombApi::class.java)
}
