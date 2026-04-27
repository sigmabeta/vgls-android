package com.vgleadsheets.ui

import com.vgleadsheets.connectivity.NetworkStatus
import com.vgleadsheets.connectivity.VglsHttpException
import com.vgleadsheets.connectivity.VglsNetworkUnavailableException
import java.io.InterruptedIOException
import java.net.SocketException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

private const val HTTP_CODE_CLASS_DIVISOR = 100
private const val HTTP_CLIENT_ERROR_CLASS = 4

fun Throwable.imageLoadErrorStringId(): StringId =
    findCause<VglsNetworkUnavailableException>()?.networkStatus?.toStringId()
        ?: findCause<VglsHttpException>()?.code?.toHttpStringId()
        ?: if (isSocketLevelError()) {
            StringId.ERROR_IMAGE_API_UNREACHABLE
        } else {
            StringId.ERROR_IMAGE_NETWORK
        }

private fun NetworkStatus.toStringId() = when (this) {
    NetworkStatus.OFFLINE -> StringId.ERROR_IMAGE_OFFLINE
    NetworkStatus.ONLINE_NO_INTERNET -> StringId.ERROR_IMAGE_NO_INTERNET
    NetworkStatus.ONLINE_API_UNREACHABLE -> StringId.ERROR_IMAGE_API_UNREACHABLE
    NetworkStatus.ONLINE -> StringId.ERROR_IMAGE_NETWORK
}

private fun Int.toHttpStringId() = when (this / HTTP_CODE_CLASS_DIVISOR) {
    HTTP_CLIENT_ERROR_CLASS -> StringId.ERROR_IMAGE_NOT_FOUND
    else -> StringId.ERROR_IMAGE_SERVER_ERROR
}

private fun Throwable.isSocketLevelError(): Boolean {
    val isNetworkInterruption = findCause { it is SocketException || it is InterruptedIOException } != null
    val isDnsOrSslError = findCause { it is UnknownHostException || it is SSLException } != null
    return isNetworkInterruption || isDnsOrSslError
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
