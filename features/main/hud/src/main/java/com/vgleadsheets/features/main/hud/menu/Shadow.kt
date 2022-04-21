package com.vgleadsheets.features.main.hud.menu

import android.view.View
import com.vgleadsheets.animation.fadeInSlightly
import com.vgleadsheets.animation.fadeOutGone

object Shadow {
    fun setToLookRightIdk(
        shadow: View,
        menuExpanded: Boolean,
        partsExpanded: Boolean
    ) {
        if (menuExpanded || partsExpanded) {
            shadow.fadeInSlightly()
        } else {
            shadow.fadeOutGone()
        }
    }
}