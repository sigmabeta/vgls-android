package com.vgleadsheets

import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import androidx.core.view.updateMargins
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.ceil
import kotlin.math.floor

@Suppress("DEPRECATION")
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

@Suppress("DEPRECATION")
fun View.setInsetListenerForOnePadding(
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

@Suppress("DEPRECATION")
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

@Suppress("DEPRECATION")
fun View.setInsetListenerForOneMargin(
    side: Side,
    offset: Int = 0
) {
    setOnApplyWindowInsetsListener { v, insets ->
        (v.layoutParams as ViewGroup.MarginLayoutParams).updateMargins(
            top = if (side == Side.TOP) insets.systemWindowInsetTop + offset else marginTop,
            left = if (side == Side.START) insets.systemWindowInsetLeft + offset else marginStart,
            right = if (side == Side.END) insets.systemWindowInsetRight + offset else marginEnd,
            bottom = if (side == Side.BOTTOM) insets.systemWindowInsetBottom + offset else marginBottom
        )
        insets
    }
}

@Suppress("DEPRECATION")
fun RecyclerView.setListsSpecialInsets(topOffset: Int, bottomOffset: Int) {
    setOnApplyWindowInsetsListener { v, insets ->
        v.updatePadding(
            top = insets.systemWindowInsetTop + topOffset,
            bottom = insets.systemWindowInsetBottom + bottomOffset
        )
        insets
    }
}

@Suppress("DEPRECATION")
fun RecyclerView.tabletSetListsSpecialInsets(topOffset: Int, bottomOffset: Int) {
    setOnApplyWindowInsetsListener { v, insets ->
        v.updatePadding(
            top = insets.systemWindowInsetTop + topOffset,
            bottom = insets.systemWindowInsetBottom + bottomOffset
        )
        insets
    }
}

fun Float.spToPx(metrics: DisplayMetrics) = (this * metrics.scaledDensity).round()
fun Float.dpToPx(metrics: DisplayMetrics) = (this * metrics.density).round()
fun Int.pxToDp(metrics: DisplayMetrics) = (this / metrics.density).round()

private fun Float.round() =
    (if (this < 0) ceil(this - ROUND_THRESHOLD) else floor(this + ROUND_THRESHOLD)).toInt()

const val ROUND_THRESHOLD = 0.5f
