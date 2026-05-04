package com.vgleadsheets.environment

import net.sigmabeta.sage.settings.environment.AppEnvironment

enum class Environment(override val url: String?) : AppEnvironment {
    PROD("https://www.vgleadsheets.com/"),
    SUPER("https://super.vgleadsheets.com/"),
    BETA("https://beta.vgleadsheets.com/"),
    FAKE(null),
}
