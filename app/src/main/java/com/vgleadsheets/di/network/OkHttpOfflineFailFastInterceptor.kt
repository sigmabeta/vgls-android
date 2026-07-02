package com.vgleadsheets.di.network

import dev.zacsweers.metro.Inject
import net.sigmabeta.sage.connectivity.NetworkStatusProvider
import net.sigmabeta.sage.connectivity.NetworkUnavailableException
import net.sigmabeta.sage.connectivity.allowsApiRequests
import okhttp3.Interceptor
import okhttp3.Response

/**
 * okhttp fail-fast interceptor for the clients that still use okhttp — Coil image loading and the
 * connectivity probe. (The VGLS data API uses ktor + com.vgleadsheets.network.offlineFailFastPlugin.)
 */
class OkHttpOfflineFailFastInterceptor @Inject constructor(
    private val networkStatusProvider: NetworkStatusProvider,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val status = networkStatusProvider.status.value
        if (!status.allowsApiRequests) {
            throw NetworkUnavailableException(
                status,
                "VGLS network unavailable ($status); skipping ${chain.request().url}",
            )
        }
        return chain.proceed(chain.request())
    }
}
