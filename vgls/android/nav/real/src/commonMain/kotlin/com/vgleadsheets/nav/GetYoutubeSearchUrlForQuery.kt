package com.vgleadsheets.nav

internal fun getYoutubeSearchUrlForQuery(query: String): String = "https://www.youtube.com/results?search_query=${query.encodeUrlQueryParam()}"

/**
 * Percent-encode a query-parameter value (RFC 3986): leave unreserved chars as-is, escape the rest
 * as %XX over the UTF-8 bytes. Multiplatform replacement for `android.net.Uri.Builder`.
 */
private fun String.encodeUrlQueryParam(): String = buildString {
    for (byte in this@encodeUrlQueryParam.encodeToByteArray()) {
        val code = byte.toInt() and 0xFF
        val ch = code.toChar()
        if (ch in 'A'..'Z' || ch in 'a'..'z' || ch in '0'..'9' || ch in "-_.~") {
            append(ch)
        } else {
            append('%')
            append(HEX[code shr 4])
            append(HEX[code and 0x0F])
        }
    }
}

private const val HEX = "0123456789ABCDEF"
