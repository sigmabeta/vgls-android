package com.vgleadsheets

import android.app.Activity
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import androidx.core.view.updatePadding
import kotlin.math.ceil
import kotlin.math.floor

fun View.setInsetListenerForPadding(
    offset: Int = 0,
    topOffset: Int = 0,
    leftOffset: Int = 0,
    rightOffset: Int = 0,
    bottomOffset: Int = 0
) {
    setOnApplyWindowInsetsListener { v, insets ->
        val systemBarInsets = WindowInsetsCompat
            .toWindowInsetsCompat(insets)
            .getInsets(WindowInsetsCompat.Type.systemBars())

        v.updatePadding(
            top = systemBarInsets.top + topOffset + offset,
            left = systemBarInsets.left + leftOffset + offset,
            right = systemBarInsets.right + rightOffset + offset,
            bottom = systemBarInsets.bottom + bottomOffset + offset
        )

        insets
    }
}

fun View.setInsetListenerForOnePadding(
    side: Side,
    offset: Int = 0
) {
    setOnApplyWindowInsetsListener { v, insets ->
        val systemBarInsets = WindowInsetsCompat
            .toWindowInsetsCompat(insets)
            .getInsets(WindowInsetsCompat.Type.systemBars())

        v.updatePadding(
            top = if (side == Side.TOP) systemBarInsets.top + offset else paddingTop,
            left = if (side == Side.START) systemBarInsets.left + offset else paddingStart,
            right = if (side == Side.END) systemBarInsets.right + offset else paddingEnd,
            bottom = if (side == Side.BOTTOM) systemBarInsets.bottom + offset else paddingBottom
        )

        insets
    }
}

fun View.setInsetForOnePadding(
    side: Side,
    offset: Int = 0
) {
    val decorView = (context as? Activity)
        ?.window
        ?.decorView ?: return

    val insets = ViewCompat.getRootWindowInsets(decorView) ?: return

    val systemBarInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())

    updatePadding(
        top = if (side == Side.TOP) systemBarInsets.top + offset else paddingTop,
        left = if (side == Side.START) systemBarInsets.left + offset else paddingStart,
        right = if (side == Side.END) systemBarInsets.right + offset else paddingEnd,
        bottom = if (side == Side.BOTTOM) systemBarInsets.bottom + offset else paddingBottom
    )
}

fun View.setInsetListenerForHeight(
    side: Side,
    offset: Int = 0
) {
    setOnApplyWindowInsetsListener { _, insets ->
        val systemBarInsets = WindowInsetsCompat
            .toWindowInsetsCompat(insets)
            .getInsets(WindowInsetsCompat.Type.systemBars())

        val newHeight = when (side) {
            Side.TOP -> systemBarInsets.top
            Side.BOTTOM -> systemBarInsets.bottom
            Side.START -> systemBarInsets.left
            Side.END -> systemBarInsets.right
        }

        updateLayoutParams { height = newHeight + offset }

        insets
    }
}

fun View.setInsetListenerForMinHeight(
    side: Side,
    offset: Int = 0
) {
    setOnApplyWindowInsetsListener { _, insets ->
        val systemBarInsets = WindowInsetsCompat
            .toWindowInsetsCompat(insets)
            .getInsets(WindowInsetsCompat.Type.systemBars())

        val newHeight = when (side) {
            Side.TOP -> systemBarInsets.top
            Side.BOTTOM -> systemBarInsets.bottom
            Side.START -> systemBarInsets.left
            Side.END -> systemBarInsets.right
        }

        minimumHeight = newHeight + offset

        insets
    }
}

fun View.setMinHeightFromInset(
    side: Side,
    offset: Int = 0
) {
    val decorView = (context as? Activity)
        ?.window
        ?.decorView ?: return

    val insets = ViewCompat.getRootWindowInsets(decorView) ?: return

    val systemBarInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())

    val newHeight = when (side) {
        Side.TOP -> systemBarInsets.top
        Side.BOTTOM -> systemBarInsets.bottom
        Side.START -> systemBarInsets.left
        Side.END -> systemBarInsets.right
    }

    minimumHeight = newHeight + offset
}

fun View.setCurrentHeightFromInset(
    side: Side,
    offset: Int = 0
) {
    val decorView = (context as? Activity)
        ?.window
        ?.decorView ?: return

    val insets = ViewCompat.getRootWindowInsets(decorView) ?: return

    val systemBarInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())

    val newHeight = when (side) {
        Side.TOP -> systemBarInsets.top
        Side.BOTTOM -> systemBarInsets.bottom
        Side.START -> systemBarInsets.left
        Side.END -> systemBarInsets.right
    }

    updateLayoutParams { height = newHeight + offset }
}

fun View.setInsetListenerForMargin(
    offset: Int = 0,
    topOffset: Int = 0,
    leftOffset: Int = 0,
    rightOffset: Int = 0,
    bottomOffset: Int = 0
) {
    setOnApplyWindowInsetsListener { v, insets ->
        val systemBarInsets = WindowInsetsCompat
            .toWindowInsetsCompat(insets)
            .getInsets(WindowInsetsCompat.Type.systemBars())

        (v.layoutParams as ViewGroup.MarginLayoutParams).updateMargins(
            top = systemBarInsets.top + topOffset + offset,
            left = systemBarInsets.left + leftOffset + offset,
            right = systemBarInsets.right + rightOffset + offset,
            bottom = systemBarInsets.bottom + bottomOffset + offset
        )
        insets
    }
}

fun View.setInsetListenerForOneMargin(
    side: Side,
    offset: Int = 0
) {
    setOnApplyWindowInsetsListener { v, insets ->
        val systemBarInsets = WindowInsetsCompat
            .toWindowInsetsCompat(insets)
            .getInsets(WindowInsetsCompat.Type.systemBars())

        (v.layoutParams as ViewGroup.MarginLayoutParams).updateMargins(
            top = if (side == Side.TOP) systemBarInsets.top + offset else marginTop,
            left = if (side == Side.START) systemBarInsets.left + offset else marginStart,
            right = if (side == Side.END) systemBarInsets.right + offset else marginEnd,
            bottom = if (side == Side.BOTTOM) systemBarInsets.bottom + offset else marginBottom
        )
        insets
    }
}

fun View.setListsSpecialInsets(bottomOffset: Int) {
    setOnApplyWindowInsetsListener { v, insets ->
        val systemBarInsets = WindowInsetsCompat
            .toWindowInsetsCompat(insets)
            .getInsets(WindowInsetsCompat.Type.systemBars())

        v.updatePadding(
            // top = systemBarInsets.top + topOffset,
            bottom = systemBarInsets.bottom + bottomOffset
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
