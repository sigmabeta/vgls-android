package com.vgleadsheets.connectivity

import kotlinx.coroutines.flow.StateFlow

interface NetworkStatusProvider {
    val status: StateFlow<NetworkStatus>
    suspend fun checkApiAvailability()
}
