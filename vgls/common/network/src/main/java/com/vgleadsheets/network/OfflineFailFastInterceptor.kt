package com.vgleadsheets.network

import net.sigmabeta.sage.connectivity.NetworkStatusProvider
import net.sigmabeta.sage.connectivity.NetworkUnavailableException
import net.sigmabeta.sage.connectivity.allowsApiRequests
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class OfflineFailFastInterceptor @Inject constructor(
    private val networkStatusProvider: NetworkStatusProvider,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val status = networkStatusProvider.status.value
        if (!status.allowsApiRequests) {
            throw NetworkUnavailableException(
                status,
                "VGLS network unavailable ($status); skipping ${chain.request().url}"
            )
        }
        return chain.proceed(chain.request())
    }
}
