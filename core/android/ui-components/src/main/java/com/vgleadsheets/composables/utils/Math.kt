package com.vgleadsheets.composables.utils

import com.google.android.material.math.MathUtils

fun partialLerpUntil(
    start: Float,
    end: Float,
    progress: Float,
    threshold: Float,
): Float {
    return if (progress < threshold) {
        start
    } else {
        val scaledProgress = (progress - threshold) / threshold
        MathUtils.lerp(start, end, scaledProgress)
    }
}

fun partialLerpAfter(
    start: Float,
    end: Float,
    progress: Float,
    threshold: Float,
): Float {
    return if (progress > threshold) {
        end
    } else {
        val scaledProgress = 1.0f - ((threshold - progress) / threshold)
        MathUtils.lerp(start, end, scaledProgress)
    }
}
