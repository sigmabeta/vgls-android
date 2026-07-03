package com.vgleadsheets.di

import android.content.Context
import com.vgleadsheets.network.VglsApi
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Named
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import net.sigmabeta.sage.android.connectivity.AndroidNetworkStatusProvider
import net.sigmabeta.sage.connectivity.NetworkStatusProvider
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.di.AppScope
import net.sigmabeta.sage.logging.Hatchet
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException

@BindingContainer
@ContributesTo(AppScope::class)
object ConnectivityModule {
    @Provides
    @SingleIn(AppScope::class)
    fun provideNetworkStatusProvider(
        context: Context,
        hatchet: Hatchet,
        @Named("ProbeOkHttp") probeClient: OkHttpClient,
        @Named("VglsApiUrl") apiBaseUrl: String?,
        dispatchers: SageDispatchers,
    ): NetworkStatusProvider = AndroidNetworkStatusProvider(
        context = context,
        hatchet = hatchet,
        dispatchers = dispatchers,
        apiProbe = {
            if (apiBaseUrl == null) {
                true
            } else {
                try {
                    val request = Request.Builder()
                        .url(apiBaseUrl + VglsApi.LAST_UPDATE_PATH)
                        .get()
                        .build()
                    probeClient.newCall(request).execute().use { it.isSuccessful }
                } catch (e: IOException) {
                    hatchet.w("VGLS API probe failed: ${e.message}")
                    false
                }
            }
        },
    )
}
