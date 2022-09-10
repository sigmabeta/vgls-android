package com.vgleadsheets.features.main.hud.menu

import android.view.View
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.vgleadsheets.animation.fadeInSlightly
import com.vgleadsheets.animation.fadeOutGone
import com.vgleadsheets.features.main.hud.HudMode
import timber.log.Timber

object HudVisibility {
    fun setToLookRightIdk(
        shadow: View,
        hudMode: HudMode,
        bottomSheet: BottomSheetBehavior<FrameLayout>
    ) {
        when (hudMode) {
            HudMode.REGULAR -> handleRegularHud(shadow, bottomSheet)
            HudMode.HIDDEN -> handleHiddenHud(shadow, bottomSheet)
            else -> handleOtherHudStatesLol(hudMode, shadow, bottomSheet)
        }
    }

    private fun handleRegularHud(
        shadow: View,
        bottomSheet: BottomSheetBehavior<FrameLayout>
    ) {
        Timber.w("Showing hud regular")
        shadow.fadeOutGone()
        bottomSheet.unhide()
    }

    private fun handleHiddenHud(
        shadow: View,
        bottomSheet: BottomSheetBehavior<FrameLayout>
    ) {
        Timber.w("Showing hud hidden")

        shadow.fadeOutGone()
        bottomSheet.hide()
    }

    private fun handleOtherHudStatesLol(
        hudMode: HudMode,
        shadow: View,
        bottomSheet: BottomSheetBehavior<FrameLayout>
    ) {
        Timber.w("Showing hud $hudMode")

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
