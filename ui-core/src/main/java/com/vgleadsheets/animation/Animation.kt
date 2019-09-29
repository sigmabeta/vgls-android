@file:Suppress("TooManyFunctions")

package com.vgleadsheets.animation

import android.util.Pair
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.TextView

const val DURATION_XSLOW = 500L
const val DURATION_SLOW = 300L
const val DURATION_QUICK = 250L
const val DURATION_X_QUICK = 100L
const val DURATION_XX_QUICK = 50L

private const val ALPHA_OPAQUE = 1.0f
private const val ALPHA_SEMI_TRANSPARENT = 0.6f
private const val ALPHA_VERY_TRANSPARENT = 0.4f
private const val ALPHA_TRANSPARENT = 0.0f

private const val SCALE_UNITY = 1.0f
private const val SCALE_NOTHING = 0.0f

const val TRANSLATION_CENTER = 0.0f

private const val INTERPOLATOR_FACTOR = 2.0f

val ACCELERATE = AccelerateInterpolator(INTERPOLATOR_FACTOR)
val DECELERATE = DecelerateInterpolator(INTERPOLATOR_FACTOR)

fun View.slideViewUpOffscreen(): ViewPropertyAnimator {
    return animate()
        .withLayer()
        .setInterpolator(DECELERATE)
        .setDuration(DURATION_SLOW)
        .translationY(-height.toFloat() * 2)
        .withEndAction { visibility = View.GONE }
}

fun View.slideViewDownOffscreen(): ViewPropertyAnimator {
    return animate()
        .withLayer()
        .setInterpolator(DECELERATE)
        .setDuration(DURATION_SLOW)
        .translationY(height.toFloat() * 2)
        .withEndAction { visibility = View.GONE }
}

fun View.slideViewOnscreen(): ViewPropertyAnimator {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }

    return animate()
        .withLayer()
        .setInterpolator(DECELERATE)
        .setDuration(DURATION_SLOW)
        .translationY(TRANSLATION_CENTER)
}

fun View.fadeInSlightly() {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }

    if (alpha != ALPHA_VERY_TRANSPARENT) {
        animate().cancel()
        animate()
            .setInterpolator(DECELERATE)
            .setDuration(DURATION_XSLOW)
            .alpha(ALPHA_VERY_TRANSPARENT)
    }
}

fun View.fadeIn() {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }

    if (alpha != ALPHA_OPAQUE) {
        animate().cancel()
        animate()
            .setInterpolator(DECELERATE)
            .setDuration(DURATION_QUICK)
            .alpha(ALPHA_OPAQUE)
    }
}

fun View.fadeOutGone() {
    if (visibility != View.GONE) {
        animate().cancel()
        animate()
            .setInterpolator(DECELERATE)
            .setDuration(DURATION_QUICK)
            .alpha(ALPHA_TRANSPARENT)
            .withEndAction {
                visibility = View.GONE

            }
    }
}

fun View.fadeOutPartially() {
    if (alpha != ALPHA_SEMI_TRANSPARENT) {
        animate().cancel()
        animate()
            .setInterpolator(ACCELERATE)
            .setDuration(DURATION_QUICK)
            .alpha(ALPHA_SEMI_TRANSPARENT)
    }
}

fun TextView.changeText(text: String) {
    if (getText() != text) {
        animate().withLayer()
            .setDuration(DURATION_XX_QUICK)
            .setInterpolator(DECELERATE)
            .alpha(ALPHA_TRANSPARENT)
            .withEndAction {
                setText(text)

                animate().withLayer()
                    .setDuration(DURATION_X_QUICK)
                    .setInterpolator(DECELERATE)
                    .alpha(ALPHA_OPAQUE)
            }
    }
}

fun View.shrinktoNothing() = animate()
    .withLayer()
    .setInterpolator(ACCELERATE)
    .setDuration(DURATION_QUICK)
    .scaleX(SCALE_NOTHING)
    .scaleY(SCALE_NOTHING)

fun View.growFromNothing() = animate()
    .withLayer()
    .setDuration(DURATION_X_QUICK)
    .setInterpolator(DECELERATE)
    .scaleX(SCALE_UNITY)
    .scaleY(SCALE_UNITY)

fun removeNullViewPairs(vararg views: Pair<View, String>?): Array<Pair<View, String>> {
    val viewsList = ArrayList<Pair<View, String>>(views.size)

    views.forEach {
        if (it != null) {
            viewsList.add(it)
        }
    }

    return viewsList.toTypedArray()
}
