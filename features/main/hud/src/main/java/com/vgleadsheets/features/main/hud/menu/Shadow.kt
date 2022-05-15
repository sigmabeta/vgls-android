package com.vgleadsheets.features.main.hud.menu

import android.view.View
import com.vgleadsheets.animation.fadeInSlightly
import com.vgleadsheets.animation.fadeOutGone
import com.vgleadsheets.features.main.hud.HudMode

object Shadow {
    fun setToLookRightIdk(
        shadow: View,
        hudMode: HudMode
    ) {
        if (hudMode != HudMode.REGULAR) {
            shadow.fadeInSlightly()
        } else {
            shadow.fadeOutGone()
        }
    }
}
