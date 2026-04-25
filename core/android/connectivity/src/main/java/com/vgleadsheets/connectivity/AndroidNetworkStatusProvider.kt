package com.vgleadsheets.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import com.vgleadsheets.logging.Hatchet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AndroidNetworkStatusProvider(
    context: Context,
    private val hatchet: Hatchet,
) : NetworkStatusProvider {
    private val _status = MutableStateFlow(NetworkStatus.OFFLINE)
    override val status: StateFlow<NetworkStatus> = _status.asStateFlow()

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

    private fun updateStatus(caps: NetworkCapabilities?) {
        setStatus(caps.toNetworkStatus())
    }

    private fun setStatus(new: NetworkStatus) {
        val old = _status.value
        if (old != new) {
            hatchet.v("$old -> $new")
            _status.value = new
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
