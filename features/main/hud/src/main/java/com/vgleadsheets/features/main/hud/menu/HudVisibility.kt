package com.vgleadsheets.features.main.hud.menu

import android.view.View
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.vgleadsheets.animation.fadeInSlightly
import com.vgleadsheets.animation.fadeOutGone
import com.vgleadsheets.features.main.hud.HudMode

object HudVisibility {
    fun setToLookRightIdk(
        shadow: View,
        hudMode: HudMode,
        bottomSheet: BottomSheetBehavior<FrameLayout>
    ) {
        when (hudMode) {
            HudMode.REGULAR -> handleRegularHud(shadow, bottomSheet)
            HudMode.HIDDEN -> handleHiddenHud(shadow, bottomSheet)
            else -> handleOtherHudStatesLol(shadow, bottomSheet)
        }
    }

    private fun handleRegularHud(
        shadow: View,
        bottomSheet: BottomSheetBehavior<FrameLayout>
    ) {
        shadow.fadeOutGone()
        bottomSheet.unhide()
    }

    private fun handleHiddenHud(
        shadow: View,
        bottomSheet: BottomSheetBehavior<FrameLayout>
    ) {
        shadow.fadeOutGone()
        bottomSheet.hide()
    }

    private fun handleOtherHudStatesLol(
        shadow: View,
        bottomSheet: BottomSheetBehavior<FrameLayout>
    ) {
        shadow.fadeInSlightly()
        bottomSheet.expand()
    }

    private fun <SheetType : View> BottomSheetBehavior<SheetType>.expand() {
        if (state == BottomSheetBehavior.STATE_COLLAPSED || state == BottomSheetBehavior.STATE_HIDDEN) {
            skipCollapsed = false
            isHideable = false

            state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun <SheetType : View> BottomSheetBehavior<SheetType>.unhide() {
        if (state == BottomSheetBehavior.STATE_HIDDEN) {
            skipCollapsed = false
            isHideable = false

            state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun <SheetType : View> BottomSheetBehavior<SheetType>.hide() {
        skipCollapsed = true
        isHideable = true
        state = BottomSheetBehavior.STATE_HIDDEN
    }
}
