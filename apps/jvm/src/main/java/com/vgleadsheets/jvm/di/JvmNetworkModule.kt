package com.vgleadsheets.jvm.di

import com.vgleadsheets.jvm.JvmNetworkStatusProvider
import com.vgleadsheets.jvm.JvmWindowFocus
import com.vgleadsheets.network.VglsApi
import com.vgleadsheets.network.offlineFailFastPlugin
import com.vgleadsheets.urlinfo.UrlInfoProvider
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Named
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import net.sigmabeta.sage.appinfo.AppInfo
import net.sigmabeta.sage.connectivity.NetworkStatusProvider
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.di.AppScope
import net.sigmabeta.sage.logging.Hatchet
import java.util.Random

/**
 * Desktop mirror of the android app's NetworkModule/ApiModule/ConnectivityModule network wiring: the
 * RNG seed (used by FakeModelGenerator), the VGLS base URLs (resolved off UrlInfoProvider), the ktor
 * HttpClient (OkHttp engine — the same one android uses; it's a JVM library), and the polling
 * [JvmNetworkStatusProvider] with its own probe client (android's ProbeOkHttp analog). android's
 * VglsOkHttp (for Coil's network fetcher) is omitted: desktop Coil only loads sheet PDFs.
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
    @Named("VgmMetadataUrl")
    fun provideVgmMetadataUrl(): String? = (System.getProperty("vgm.metadata.url") ?: System.getenv("VGM_METADATA_URL"))
        ?.trim()
        ?.takeIf { it.isNotEmpty() }

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

    /**
     * Probe client for the API reachability check. Deliberately NOT the main [HttpClient]: that one
     * installs the offline fail-fast plugin, which would reject the very probe that promotes
     * ONLINE_API_UNREACHABLE back to ONLINE.
     */
    @Provides
    @SingleIn(AppScope::class)
    @Named("ProbeHttpClient")
    fun provideProbeHttpClient(): HttpClient = HttpClient(OkHttp) {
        install(HttpTimeout) {
            requestTimeoutMillis = PROBE_TIMEOUT_MS
        }
    }

    @Provides
    @SingleIn(AppScope::class)
    fun provideWindowFocus(): JvmWindowFocus = JvmWindowFocus()

    @Provides
    @SingleIn(AppScope::class)
    fun provideNetworkStatusProvider(
        windowFocus: JvmWindowFocus,
        hatchet: Hatchet,
        dispatchers: SageDispatchers,
        @Named("ProbeHttpClient") probeClient: HttpClient,
        @Named("VglsApiUrl") apiBaseUrl: String?,
    ): NetworkStatusProvider = JvmNetworkStatusProvider(
        hatchet = hatchet,
        dispatchers = dispatchers,
        apiProbe = {
            // No API configured (fake environment) → nothing to probe; treat as reachable.
            apiBaseUrl == null || probeClient.get(apiBaseUrl + VglsApi.LAST_UPDATE_PATH).status.isSuccess()
        },
        windowFocused = windowFocus.focused,
    )

    const val SEED_RANDOM_NUMBER_GENERATOR = 12345L
    private const val PROBE_TIMEOUT_MS = 5_000L
}
