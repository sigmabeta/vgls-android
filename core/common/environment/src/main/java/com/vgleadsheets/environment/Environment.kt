package com.vgleadsheets.environment

enum class Environment(
    val url: String?
) {
    PROD("https://www.vgleadsheets.com/"),
    SUPER("https://super.vgleadsheets.com/"),
    BETA("https://beta.vgleadsheets.com/"),
    FAKE(null),
}
