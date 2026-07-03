package com.vgleadsheets.strings

import net.sigmabeta.sage.connectivity.HttpException
import net.sigmabeta.sage.connectivity.NetworkStatus
import net.sigmabeta.sage.connectivity.NetworkUnavailableException

// Socket/DNS/SSL-level failures identified by exception class name — multiplatform (no java.net /
// javax.net imports), and covers both the android okhttp stack and the desktop ktor stack.
private val SOCKET_LEVEL_EXCEPTION_NAMES = setOf(
    "SocketException",
    "SocketTimeoutException",
    "InterruptedIOException",
    "ConnectException",
    "UnknownHostException",
    "SSLException",
    "SSLHandshakeException",
    "ConnectTimeoutException",
    "HttpRequestTimeoutException",
)

private const val HTTP_CODE_CLASS_DIVISOR = 100
private const val HTTP_CLIENT_ERROR_CLASS = 4

fun Throwable.imageLoadErrorStringId(): VglsStringId = findCause<NetworkUnavailableException>()
    ?.networkStatus
    ?.toStringId()
    ?: findCause<HttpException>()?.code?.toHttpStringId()
    ?: if (isSocketLevelError()) {
        VglsStringId.ERROR_IMAGE_API_UNREACHABLE
    } else {
        VglsStringId.ERROR_IMAGE_NETWORK
    }

private fun NetworkStatus.toStringId() = when (this) {
    NetworkStatus.OFFLINE -> VglsStringId.ERROR_IMAGE_OFFLINE
    NetworkStatus.ONLINE_NO_INTERNET -> VglsStringId.ERROR_IMAGE_NO_INTERNET
    NetworkStatus.ONLINE_API_UNREACHABLE -> VglsStringId.ERROR_IMAGE_API_UNREACHABLE
    NetworkStatus.ONLINE -> VglsStringId.ERROR_IMAGE_NETWORK
}

private fun Int.toHttpStringId() = when (this / HTTP_CODE_CLASS_DIVISOR) {
    HTTP_CLIENT_ERROR_CLASS -> VglsStringId.ERROR_IMAGE_NOT_FOUND
    else -> VglsStringId.ERROR_IMAGE_SERVER_ERROR
}

private fun Throwable.isSocketLevelError(): Boolean = findCause { it::class.simpleName in SOCKET_LEVEL_EXCEPTION_NAMES } != null

private inline fun <reified T : Throwable> Throwable.findCause(): T? = findCause { it is T } as T?

private fun Throwable.findCause(predicate: (Throwable) -> Boolean): Throwable? {
    var current: Throwable? = this
    val seen = mutableSetOf<Throwable>()
    while (current != null && seen.add(current)) {
        if (predicate(current)) return current
        current = current.cause
    }
    return null
}
