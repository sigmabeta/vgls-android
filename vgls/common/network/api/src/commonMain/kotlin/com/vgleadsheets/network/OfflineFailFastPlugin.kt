package com.vgleadsheets.network

import io.ktor.client.plugins.api.createClientPlugin
import net.sigmabeta.sage.connectivity.NetworkStatusProvider
import net.sigmabeta.sage.connectivity.NetworkUnavailableException
import net.sigmabeta.sage.connectivity.allowsApiRequests

/**
 * Ktor client plugin that fails a request fast (before hitting the network) when VGLS connectivity
 * is unavailable — the multiplatform replacement for the old okhttp OfflineFailFastInterceptor.
 */
fun offlineFailFastPlugin(networkStatusProvider: NetworkStatusProvider) = createClientPlugin("OfflineFailFast") {
        onRequest { request, _ ->
            val status = networkStatusProvider.status.value
            if (!status.allowsApiRequests) {
                throw NetworkUnavailableException(
                    status,
                    "VGLS network unavailable ($status); skipping ${request.url}",
                )
            }
        }
    }
