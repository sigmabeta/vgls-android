package com.vgleadsheets.ui

import com.vgleadsheets.connectivity.NetworkStatus
import com.vgleadsheets.connectivity.VglsHttpException
import com.vgleadsheets.connectivity.VglsNetworkUnavailableException
import java.io.InterruptedIOException
import java.net.SocketException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

fun Throwable.imageLoadErrorStringId(): StringId {
    val networkStatus = findCause<VglsNetworkUnavailableException>()?.networkStatus
    if (networkStatus != null) {
        return when (networkStatus) {
            NetworkStatus.OFFLINE -> StringId.ERROR_IMAGE_OFFLINE
            NetworkStatus.ONLINE_NO_INTERNET -> StringId.ERROR_IMAGE_NO_INTERNET
            NetworkStatus.ONLINE_API_UNREACHABLE -> StringId.ERROR_IMAGE_API_UNREACHABLE
            NetworkStatus.ONLINE -> StringId.ERROR_IMAGE_NETWORK
        }
    }
    val httpCode = findCause<VglsHttpException>()?.code
    if (httpCode != null) {
        return when (httpCode / 100) {
            4 -> StringId.ERROR_IMAGE_NOT_FOUND
            else -> StringId.ERROR_IMAGE_SERVER_ERROR
        }
    }
    if (findCause { it is SocketException || it is InterruptedIOException || it is UnknownHostException || it is SSLException } != null) {
        return StringId.ERROR_IMAGE_API_UNREACHABLE
    }
    return StringId.ERROR_IMAGE_NETWORK
}

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
