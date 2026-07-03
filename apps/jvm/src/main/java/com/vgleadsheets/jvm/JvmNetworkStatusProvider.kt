package com.vgleadsheets.jvm

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import net.sigmabeta.sage.connectivity.NetworkStatus
import net.sigmabeta.sage.connectivity.NetworkStatusProvider

/**
 * Desktop [NetworkStatusProvider] — assumes an online connection (no per-OS reachability probing yet).
 * The ktor offline-fail-fast plugin therefore never short-circuits on desktop.
 */
class JvmNetworkStatusProvider : NetworkStatusProvider {
    override val status: StateFlow<NetworkStatus> = MutableStateFlow(NetworkStatus.ONLINE)

    override suspend fun checkApiAvailability() = Unit
}
