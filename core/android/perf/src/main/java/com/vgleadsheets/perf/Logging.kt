@file:Suppress("MaxLineLength")

package com.vgleadsheets.perf

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import com.vgleadsheets.logging.BasicHatchet
import com.vgleadsheets.logging.Hatchet
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.measureTime
import kotlin.time.toDuration

val LocalLogger = staticCompositionLocalOf<Hatchet> { BasicHatchet() }

private const val THRESHOLD_WARNING_MS_SCREEN_PREVIEW = 1
private const val THRESHOLD_ERROR_MS_SCREEN_PREVIEW = 5

private const val THRESHOLD_WARNING_MS_SCREEN_DEVICE = 7
private const val THRESHOLD_ERROR_MS_SCREEN_DEVICE = 12

private const val THRESHOLD_WARNING_US_COMPONENT_DEVICE = 1500
private const val THRESHOLD_ERROR_US_COMPONENT_DEVICE = 3000

val DURATION_THRESHOLD_WARNING_SCREEN_DEVICE = THRESHOLD_WARNING_MS_SCREEN_DEVICE.toDuration(DurationUnit.MILLISECONDS)
val DURATION_THRESHOLD_ERROR_SCREEN_DEVICE = THRESHOLD_ERROR_MS_SCREEN_DEVICE.toDuration(DurationUnit.MILLISECONDS)

val DURATION_THRESHOLD_WARNING_SCREEN_PREVIEW = THRESHOLD_WARNING_MS_SCREEN_PREVIEW.toDuration(DurationUnit.MILLISECONDS)
val DURATION_THRESHOLD_ERROR_SCREEN_PREVIEW = THRESHOLD_ERROR_MS_SCREEN_PREVIEW.toDuration(DurationUnit.MILLISECONDS)

val DURATION_THRESHOLD_WARNING_COMPONENT_DEVICE = THRESHOLD_WARNING_US_COMPONENT_DEVICE.toDuration(DurationUnit.MICROSECONDS)
val DURATION_THRESHOLD_ERROR_COMPONENT_DEVICE = THRESHOLD_ERROR_US_COMPONENT_DEVICE.toDuration(DurationUnit.MICROSECONDS)

@Composable
fun WithMeasurementScreen(
    title: String,
    warningThreshold: Duration,
    errorThreshold: Duration,
    content: @Composable () -> Unit
) {
    if (!BuildConfig.DEBUG) {
        return
    }

    val duration = measureTime {
        content()
    }

    val severity = when {
        duration < warningThreshold -> Log.INFO
        duration < errorThreshold -> Log.WARN
        else -> Log.ERROR
    }

    val hatchet = LocalLogger.current

    hatchet.log(
        severity = severity,
        message = "Composing screen with title: $title took $duration."
    )
}

@Composable
fun WithMeasurementComponent(
    name: String,
    warningThreshold: Duration,
    errorThreshold: Duration,
    content: @Composable () -> Unit
) {
    if (!BuildConfig.DEBUG) {
        return
    }

    val duration = measureTime {
        content()
    }

    val severity = when {
        duration < warningThreshold -> return
        duration < errorThreshold -> Log.WARN
        else -> Log.ERROR
    }

    val hatchet = LocalLogger.current

    hatchet.log(
        severity = severity,
        message = "Composing component with name: $name took $duration."
    )
}
