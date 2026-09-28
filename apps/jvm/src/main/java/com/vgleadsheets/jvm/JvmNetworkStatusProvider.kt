package com.vgleadsheets.jvm

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import net.sigmabeta.sage.connectivity.NetworkStatus
import net.sigmabeta.sage.connectivity.NetworkStatusProvider
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.logging.Hatchet
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

/**
 * Desktop [NetworkStatusProvider]. The JVM has no OS connectivity callbacks, so this polls — but only
 * while the window is focused ([windowFocused]): on gaining focus it checks immediately, then every
 * [POLL_INTERVAL_MS] while focus is held, and stops when focus is lost. Each check "pings" a few
 * well-known public DNS resolvers — a TCP connect to port 53, since real ICMP needs root on the JVM
 * (`InetAddress.isReachable` silently falls back to TCP port 7, which these hosts don't answer).
 * Any host reachable → internet is up; none → [NetworkStatus.OFFLINE].
 *
 * On top of that it mirrors android's state machine: a transition to [NetworkStatus.ONLINE] launches
 * [apiProbe], which demotes to [NetworkStatus.ONLINE_API_UNREACHABLE] if the VGLS API doesn't answer,
 * and [checkApiAvailability] re-probes on demand. There's no desktop analog of
 * [NetworkStatus.ONLINE_NO_INTERNET] (a captive portal just reads as OFFLINE).
 */
class JvmNetworkStatusProvider(
    private val hatchet: Hatchet,
    dispatchers: SageDispatchers,
    private val apiProbe: suspend () -> Boolean,
    private val windowFocused: StateFlow<Boolean>,
    private val hosts: List<String> = DEFAULT_HOSTS,
) : NetworkStatusProvider {
    // Optimistic until the first check lands: nothing retries a request the offline fail-fast plugin
    // rejects, so a pessimistic OFFLINE start would fail startup requests (update check, sheet loads).
    // Starting ONLINE just means those behave as a plain network call would if we are in fact offline.
    private val _status = MutableStateFlow(NetworkStatus.ONLINE)
    override val status: StateFlow<NetworkStatus> = _status.asStateFlow()

    private val networkDispatcher = dispatchers.network
    private val scope = CoroutineScope(SupervisorJob() + networkDispatcher)
    private val probeMutex = Mutex()
    private var probeJob: Job? = null

    init {
        // Initial check, off the calling thread (this is constructed lazily, often on the UI thread).
        // The status already reads ONLINE, so a reachable result wouldn't transition — probe the API
        // explicitly instead.
        scope.launch {
            if (anyHostReachable()) launchProbe() else setStatus(NetworkStatus.OFFLINE)
        }

        scope.launch {
            // collectLatest cancels the running poll loop as soon as focus is lost.
            windowFocused.collectLatest { focused ->
                if (!focused) return@collectLatest
                while (true) {
                    updateStatus(anyHostReachable())
                    delay(POLL_INTERVAL_MS)
                }
            }
        }
    }

    override suspend fun checkApiAvailability() {
        if (_status.value == NetworkStatus.OFFLINE) return
        probeMutex.withLock { runProbe() }
    }

    private suspend fun anyHostReachable(): Boolean = coroutineScope {
        hosts.map { host -> async { isReachable(host) } }.awaitAll().any { it }
    }

    private suspend fun isReachable(host: String): Boolean = withContext(networkDispatcher) {
        try {
            Socket().use { it.connect(InetSocketAddress(host, DNS_PORT), CONNECT_TIMEOUT_MS) }
            true
        } catch (_: IOException) {
            // Expected every poll while offline; the resulting status transition is what gets logged.
            false
        }
    }

    private fun updateStatus(internetReachable: Boolean) {
        when {
            !internetReachable -> setStatus(NetworkStatus.OFFLINE)

            // Already online (or online with the API down): keep the API verdict until re-probed.
            _status.value == NetworkStatus.OFFLINE -> setStatus(NetworkStatus.ONLINE)
        }
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

    private companion object {
        val DEFAULT_HOSTS = listOf("1.1.1.1", "8.8.8.8", "9.9.9.9")
        const val DNS_PORT = 53
        const val CONNECT_TIMEOUT_MS = 1_500
        const val POLL_INTERVAL_MS = 10_000L
    }
}
