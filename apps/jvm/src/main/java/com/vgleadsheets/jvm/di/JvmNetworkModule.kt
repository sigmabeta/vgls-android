package com.vgleadsheets.jvm.di

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
import net.sigmabeta.sage.appinfo.AppInfo
import net.sigmabeta.sage.connectivity.NetworkStatusProvider
import net.sigmabeta.sage.di.AppScope
import net.sigmabeta.sage.logging.Hatchet
import java.util.Random

/**
 * Desktop mirror of the android app's NetworkModule/ApiModule network wiring: the RNG seed (used by
 * FakeModelGenerator), the VGLS base URLs (resolved off UrlInfoProvider), and the ktor HttpClient
 * (OkHttp engine — the same one android uses; it's a JVM library). The standalone okhttp clients
 * (ProbeOkHttp/VglsOkHttp) android needs for Coil + the connectivity probe are omitted: desktop uses
 * the always-online JvmNetworkStatusProvider and skips the Coil image pipeline.
 */
@BindingContainer
@ContributesTo(AppScope::class)
object JvmNetworkModule {
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
        install(offlineFailFastPlugin(networkStatusProvider))
    }

    const val SEED_RANDOM_NUMBER_GENERATOR = 12345L
}
