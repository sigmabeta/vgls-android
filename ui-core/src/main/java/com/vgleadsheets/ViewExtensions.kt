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

}fun View.setInsetListenerForOnePadding(
    side: Side,
    offset: Int = 0
) {
    setOnApplyWindowInsetsListener { v, insets ->
        v.updatePadding(
            top = if (side == Side.TOP) insets.systemWindowInsetTop + offset else paddingTop,
            left = if (side == Side.START) insets.systemWindowInsetLeft + offset else paddingStart,
            right = if (side == Side.END) insets.systemWindowInsetRight + offset else paddingEnd,
            bottom = if (side == Side.BOTTOM) insets.systemWindowInsetBottom + offset else paddingBottom
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

enum class Side {
    TOP,
    BOTTOM,
    START,
    END
}
