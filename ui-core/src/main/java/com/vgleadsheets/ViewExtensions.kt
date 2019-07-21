package com.vgleadsheets

import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateMargins
import androidx.core.view.updatePadding

fun View.setInsetListenerForPadding(
    offset: Int = 0,
    topOffset: Int = 0,
    leftOffset: Int = 0,
    rightOffset: Int = 0,
    bottomOffset: Int = 0
) {
    setOnApplyWindowInsetsListener { v, insets ->
        v.updatePadding(
            top = insets.systemWindowInsetTop + topOffset + offset,
            left = insets.systemWindowInsetLeft + leftOffset + offset,
            right = insets.systemWindowInsetRight + rightOffset + offset,
            bottom = insets.systemWindowInsetBottom + bottomOffset + offset
        )
        insets
    }
}

fun View.setInsetListenerForMargin(
    offset: Int = 0,
    topOffset: Int = 0,
    leftOffset: Int = 0,
    rightOffset: Int = 0,
    bottomOffset: Int = 0
) {
    setOnApplyWindowInsetsListener { v, insets ->
        (v.layoutParams as ViewGroup.MarginLayoutParams).updateMargins(
            top = insets.systemWindowInsetTop + topOffset + offset,
            left = insets.systemWindowInsetLeft + leftOffset + offset,
            right = insets.systemWindowInsetRight + rightOffset + offset,
            bottom = insets.systemWindowInsetBottom + bottomOffset + offset
        )
        insets
    }
}