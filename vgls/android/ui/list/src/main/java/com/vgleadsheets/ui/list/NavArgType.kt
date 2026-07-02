package com.vgleadsheets.ui.list

import androidx.navigation.NavType
import net.sigmabeta.sage.nav.ArgType

/**
 * Maps a SAGE [ArgType] to an AndroidX-nav [NavType]. Reclaimed into VGLS because SAGE dropped its
 * `ArgType.toNavType()` helper when it moved to Voyager; VGLS still uses AndroidX Navigation until
 * the Voyager migration (project phase 5).
 */
fun ArgType.toNavType(): NavType<*> = when (this) {
    ArgType.NONE -> NavType.LongType
    // Will produce a 0
    ArgType.LONG -> NavType.LongType
    ArgType.STRING -> NavType.StringType
    ArgType.TWO -> NavType.LongType
}
