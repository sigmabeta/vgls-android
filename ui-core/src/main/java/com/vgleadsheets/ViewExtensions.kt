package com.vgleadsheets

import android.view.View
import androidx.core.view.updatePadding

fun View.setSimpleInsetListener() {
    setOnApplyWindowInsetsListener { v, insets ->
        v.updatePadding(
            top = insets.systemWindowInsetTop,
            left = insets.systemWindowInsetLeft,
            right = insets.systemWindowInsetRight,
            bottom = insets.systemWindowInsetBottom
        )
        insets
    }
}