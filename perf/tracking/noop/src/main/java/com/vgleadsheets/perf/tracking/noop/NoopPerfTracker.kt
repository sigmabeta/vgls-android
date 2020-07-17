package com.vgleadsheets.perf.tracking.noop

import com.vgleadsheets.perf.tracking.common.PerfStage
import com.vgleadsheets.perf.tracking.common.PerfTracker
import com.vgleadsheets.tracking.TrackingScreen
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import timber.log.Timber
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class NoopPerfTracker : PerfTracker {
    private val activeTraces =
        HashMap<TrackingScreen, MutableMap<PerfStage, Long?>?>(TrackingScreen.values().size)

    private val failureTimers =
        HashMap<TrackingScreen, Disposable?>(TrackingScreen.values().size)

    override fun start(trackingScreen: TrackingScreen) {
        // Check if an active trace list exists
        if (activeTraces[trackingScreen] != null) {
            throw IllegalStateException(
                "Active trace list from a previous instance of screen $trackingScreen " +
                        "still exists!"
            )
        }

        // Create a new list of active traces
        val newTraces = HashMap<PerfStage, Long?>(PerfStage.values().size)

        PerfStage.values().forEach { stage ->
            newTraces[stage] = System.currentTimeMillis()
        }

        // Add it to the hashmap
        activeTraces[trackingScreen] = newTraces
        Timber.d("Starting timing for $trackingScreen...")

        startFailureTimer(trackingScreen)
    }

    override fun onViewCreated(trackingScreen: TrackingScreen) =
        finishTrace(trackingScreen, PerfStage.VIEW_CREATED)

    override fun onTitleRendered(trackingScreen: TrackingScreen) =
        finishTrace(trackingScreen, PerfStage.TITLE_RENDERED)

    override fun onTransitionStarted(trackingScreen: TrackingScreen) =
        finishTrace(trackingScreen, PerfStage.TRANSITION_START)

    override fun onTransitionEnded(trackingScreen: TrackingScreen) =
        finishTrace(trackingScreen, PerfStage.TRANSITION_END)

    override fun onPartialLoad(trackingScreen: TrackingScreen) =
        finishTrace(trackingScreen, PerfStage.PARTIAL_LOAD)

    override fun onFullLoad(trackingScreen: TrackingScreen): Long? {
        val duration = finishTrace(trackingScreen, PerfStage.FULL_LOAD) ?: return null

        Timber.d("Successful load of $trackingScreen in $duration ms!")
        removeTracesFor(trackingScreen, true)

        return duration
    }

    override fun cancel(trackingScreen: TrackingScreen) {
        Timber.w("Cancelling timing for $trackingScreen...")
        removeTracesFor(trackingScreen, false)
    }

    private fun finishTrace(trackingScreen: TrackingScreen, perfStage: PerfStage): Long? {
        val tracesForScreen = activeTraces[trackingScreen]
            ?: throw IllegalStateException("Traces for $trackingScreen have already been cleared!")

        val startTime = tracesForScreen[perfStage]
            ?: throw IllegalStateException("Trace $trackingScreen:$perfStage has already been stopped!")

        tracesForScreen[perfStage] = null

        val duration = System.currentTimeMillis() - startTime

        Timber.v("Duration for $trackingScreen:$perfStage: $duration ms ")
        return duration
    }

    private fun removeTracesFor(trackingScreen: TrackingScreen, shouldBeEmpty: Boolean) {
        val tracesForThisScreen = activeTraces[trackingScreen]

        tracesForThisScreen
            ?: throw IllegalStateException("Traces for $trackingScreen have already been cleared!")

        val notClearedTraces = tracesForThisScreen.entries
            .map { if (it.value == null) null else it }
            .filterNotNull()
            .map { it.key }

        if (notClearedTraces.isNotEmpty()) {
            val emptinessMessage = "Missing traces for $trackingScreen: ${notClearedTraces.joinToString()}."

            if (shouldBeEmpty) {
                throw IllegalStateException(emptinessMessage)
            } else {
                Timber.i(emptinessMessage)
            }
        }

        activeTraces[trackingScreen] = null

        stopFailureTimer(trackingScreen)
    }

    private fun startFailureTimer(trackingScreen: TrackingScreen) {
        val timer = Observable.timer(TIMEOUT_SCREEN_LOAD, TimeUnit.MILLISECONDS)
            .subscribe {
                throw TimeoutException(
                    "Waited $TIMEOUT_SCREEN_LOAD ms, but still haven't received " +
                            "all timing events."
                )
            }
        failureTimers[trackingScreen] = timer
    }

    private fun stopFailureTimer(trackingScreen: TrackingScreen) {
        val timer = failureTimers[trackingScreen]
            ?: throw IllegalStateException("Failure timer for $trackingScreen has already been stopped!")

        timer.dispose()

        failureTimers[trackingScreen] = null
    }

    companion object {
        const val TIMEOUT_SCREEN_LOAD = 10_000L
    }
}