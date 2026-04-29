package com.vgleadsheets.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import net.sigmabeta.sage.coroutines.VglsDispatchers
import net.sigmabeta.sage.logging.Hatchet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.IOException

class AndroidNetworkStatusProvider(
    context: Context,
    private val hatchet: Hatchet,
    dispatchers: VglsDispatchers,
    private val apiProbe: suspend () -> Boolean,
) : NetworkStatusProvider {
    private val _status = MutableStateFlow(NetworkStatus.OFFLINE)
    override val status: StateFlow<NetworkStatus> = _status.asStateFlow()

    private val scope = CoroutineScope(SupervisorJob() + dispatchers.network)
    private val probeMutex = Mutex()
    private var probeJob: Job? = null

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            updateStatus(connectivityManager.getNetworkCapabilities(network))
        }

        override fun onLost(network: Network) {
            setStatus(NetworkStatus.OFFLINE)
        }

        override fun onCapabilitiesChanged(network: Network, caps: NetworkCapabilities) {
            updateStatus(caps)
        }
    }

    init {
        // Seed from current state before registering so consumers don't see stale OFFLINE.
        // activeNetwork requires API 23; on older devices the first callback fires immediately.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork
            updateStatus(activeNetwork?.let { connectivityManager.getNetworkCapabilities(it) })
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(callback)
        } else {
            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
            connectivityManager.registerNetworkCallback(request, callback)
        }
    }

    override suspend fun checkApiAvailability() {
        val current = _status.value
        if (current == NetworkStatus.OFFLINE || current == NetworkStatus.ONLINE_NO_INTERNET) return
        probeMutex.withLock { runProbe() }
    }

    private fun updateStatus(caps: NetworkCapabilities?) {
        setStatus(caps.toNetworkStatus())
    }

    private fun setStatus(new: NetworkStatus) {
        val old = _status.value
        if (old == new) return

        hatchet.v("$old -> $new")
        _status.value = new

        when (new) {
            NetworkStatus.ONLINE -> launchProbe()

            NetworkStatus.ONLINE_API_UNREACHABLE -> Unit

            NetworkStatus.OFFLINE,
            NetworkStatus.ONLINE_NO_INTERNET -> {
                probeJob?.cancel()
                probeJob = null
            }
        }
    }

    private fun launchProbe() {
        probeJob?.cancel()
        probeJob = scope.launch {
            probeMutex.withLock { runProbe() }
        }
    }

    private suspend fun runProbe() {
        val success = try {
            apiProbe()
        } catch (e: IOException) {
            hatchet.d("API probe failed: ${e.message}")
            false
        }
        val current = _status.value
        if (success) {
            if (current == NetworkStatus.ONLINE_API_UNREACHABLE) setStatus(NetworkStatus.ONLINE)
        } else {
            if (current == NetworkStatus.ONLINE) setStatus(NetworkStatus.ONLINE_API_UNREACHABLE)
        }
    }

    private fun NetworkCapabilities?.toNetworkStatus(): NetworkStatus {
        if (this == null || !hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
            return NetworkStatus.OFFLINE
        }
        return when {
            // NET_CAPABILITY_VALIDATED not available before API 23; assume ONLINE if transport is present.
            Build.VERSION.SDK_INT < Build.VERSION_CODES.M -> NetworkStatus.ONLINE

            hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) -> NetworkStatus.ONLINE

            else -> NetworkStatus.ONLINE_NO_INTERNET
        }
    }
}
