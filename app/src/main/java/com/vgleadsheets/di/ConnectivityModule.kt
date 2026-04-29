package com.vgleadsheets.di

import android.content.Context
import com.vgleadsheets.connectivity.AndroidNetworkStatusProvider
import net.sigmabeta.sage.connectivity.NetworkStatusProvider
import net.sigmabeta.sage.coroutines.VglsDispatchers
import net.sigmabeta.sage.logging.Hatchet
import com.vgleadsheets.network.VglsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ConnectivityModule {
    @Provides
    @Singleton
    fun provideNetworkStatusProvider(
        @ApplicationContext context: Context,
        hatchet: Hatchet,
        @Named("ProbeOkHttp") probeClient: OkHttpClient,
        @Named("VglsApiUrl") apiBaseUrl: String?,
        dispatchers: VglsDispatchers,
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
