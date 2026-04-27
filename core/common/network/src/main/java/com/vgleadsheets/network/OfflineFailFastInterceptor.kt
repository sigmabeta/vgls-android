package com.vgleadsheets.network

import com.vgleadsheets.connectivity.NetworkStatusProvider
import com.vgleadsheets.connectivity.VglsNetworkUnavailableException
import com.vgleadsheets.connectivity.allowsVglsRequests
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class OfflineFailFastInterceptor @Inject constructor(
    private val networkStatusProvider: NetworkStatusProvider,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val status = networkStatusProvider.status.value
        if (!status.allowsVglsRequests) {
            throw VglsNetworkUnavailableException(
                status,
                "VGLS network unavailable ($status); skipping ${chain.request().url}"
            )
        }
        return chain.proceed(chain.request())
    }
}
