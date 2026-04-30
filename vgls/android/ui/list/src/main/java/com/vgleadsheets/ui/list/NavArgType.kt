package com.vgleadsheets.ui.list

import androidx.navigation.NavType
import net.sigmabeta.sage.nav.ArgType

fun ArgType.toNavType(): NavType<*> = when (this) {
        ArgType.NONE -> NavType.LongType

        // Will produce a 0
        ArgType.LONG -> NavType.LongType

        ArgType.STRING -> NavType.StringType

        ArgType.TWO -> NavType.LongType
    }
