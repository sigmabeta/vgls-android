package com.vgleadsheets.di.network

import com.vgleadsheets.network.offlineFailFastPlugin
import com.vgleadsheets.urlinfo.UrlInfoProvider
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Named
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import com.vgleadsheets.di.HatchetOkHttpLogger
import net.sigmabeta.sage.appinfo.AppInfo
import net.sigmabeta.sage.connectivity.NetworkStatusProvider
import net.sigmabeta.sage.di.AppScope
import net.sigmabeta.sage.logging.Hatchet
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
    fun provideVglsUrl(urlInfoProvider: UrlInfoProvider): String? = runBlocking {
        urlInfoProvider.urlInfoFlow.first { it.loaded }.baseBaseUrl
    }

    @Provides
    @Named("VglsApiUrl")
    @SingleIn(AppScope::class)
    fun provideVglsApiUrl(urlInfoProvider: UrlInfoProvider): String? = runBlocking {
        urlInfoProvider.urlInfoFlow.first { it.loaded }.apiBaseUrl
    }

    @Provides
    @Named("VglsImageUrl")
    @SingleIn(AppScope::class)
    fun provideVglsImageUrl(urlInfoProvider: UrlInfoProvider): String? = runBlocking {
        urlInfoProvider.urlInfoFlow.first { it.loaded }.imageBaseUrl
    }

    @Provides
    @Named("VglsPdfUrl")
    @SingleIn(AppScope::class)
    fun provideVglsPdfUrl(urlInfoProvider: UrlInfoProvider): String? = runBlocking {
        urlInfoProvider.urlInfoFlow.first { it.loaded }.pdfBaseUrl
    }

    @Provides
    @SingleIn(AppScope::class)
    fun provideHttpClient(
        appInfo: AppInfo,
        networkStatusProvider: NetworkStatusProvider,
        hatchet: Hatchet,
    ): HttpClient = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        if (appInfo.isDebug) {
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        hatchet.d(message)
                    }
                }
                level = LogLevel.HEADERS
            }
        }
        // Fail fast (before hitting the network) when VGLS connectivity is unavailable.
        install(offlineFailFastPlugin(networkStatusProvider))
    }

    // --- okhttp clients for the consumers that still use okhttp: Coil image loading + the
    //     connectivity probe. (The VGLS data API above uses ktor.) ---

    @Provides
    @SingleIn(AppScope::class)
    @Named("ProbeOkHttp")
    fun provideProbeOkClient(
        appInfo: AppInfo,
        @Named("HttpLoggingInterceptor") logger: Interceptor,
    ): OkHttpClient = if (appInfo.isDebug) {
        OkHttpClient.Builder().addNetworkInterceptor(logger).build()
    } else {
        OkHttpClient()
    }

    @Provides
    @SingleIn(AppScope::class)
    @Named("VglsOkHttp")
    fun provideVglsOkClient(
        @Named("ProbeOkHttp") base: OkHttpClient,
        failFast: OkHttpOfflineFailFastInterceptor,
    ): OkHttpClient = base.newBuilder().addInterceptor(failFast).build()

    @Provides
    fun provideHatchetLogger(hatchet: Hatchet): HatchetOkHttpLogger = HatchetOkHttpLogger(hatchet)

    @Provides
    @Named("HttpLoggingInterceptor")
    fun provideHttpLoggingInterceptor(hatchetOkHttpLogger: HatchetOkHttpLogger): Interceptor {
        val logger = HttpLoggingInterceptor(hatchetOkHttpLogger)
        logger.level = HttpLoggingInterceptor.Level.HEADERS
        return logger
    }

    const val SEED_RANDOM_NUMBER_GENERATOR = 12345L
}
