package com.vgleadsheets.common.debug

enum class NetworkEndpoint(
    val url: String?
) {
    PROD("https://www.vgleadsheets.com/"),
    SUPER("https://super.vgleadsheets.com/"),
    BETA("https://beta.vgleadsheets.com/"),
    MOCK(null),
}
