package com.vgleadsheets.perf.tracking.impl

import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace
import com.vgleadsheets.perf.tracking.common.PerfStage
import com.vgleadsheets.perf.tracking.common.PerfTracker
import com.vgleadsheets.tracking.Tracker
import com.vgleadsheets.tracking.TrackingScreen

class PerfTrackerImpl(
    private val firebase: FirebasePerformance,
    private val tracker: Tracker
) : PerfTracker {

    private val activeTraces =
        HashMap<TrackingScreen, MutableMap<PerfStage, Trace?>?>(TrackingScreen.values().size)

    override fun start(trackingScreen: TrackingScreen) {
        // Check if an active trace list exists
        if (activeTraces[trackingScreen] != null) {
            tracker.logError(
                "Active trace list from a previous instance of screen $trackingScreen " +
                        "still exists!"
            )
        }

        // Create a new list of active traces
        val newTraces = HashMap<PerfStage, Trace?>(PerfStage.values().size)

        PerfStage.values().forEach { stage ->
            val newTrace = firebase.newTrace("$trackingScreen:$stage")
            newTraces[stage] = newTrace
            newTrace.start()
        }

        // Add it to the hashmap
        activeTraces[trackingScreen] = newTraces
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

        removeTracesFor(trackingScreen)

        return duration
    }

    override fun cancel(trackingScreen: TrackingScreen) {
        removeTracesFor(trackingScreen)
    }

    private fun finishTrace(trackingScreen: TrackingScreen, perfStage: PerfStage): Long? {
        val tracesForScreen = activeTraces[trackingScreen]
        if (tracesForScreen == null) {
            tracker.logError("Traces for $trackingScreen have already been cleared!")
            return null
        }

        val trace = tracesForScreen[perfStage]

        if (trace == null) {
            tracker.logError("Trace $trackingScreen:$perfStage has already been stopped!")
            return null
        }

        trace.stop()
        tracesForScreen[perfStage] = null

        return trace.getLongMetric("duration")
    }

    private fun removeTracesFor(trackingScreen: TrackingScreen) {
        val tracesForScreen = activeTraces[trackingScreen]
        if (tracesForScreen == null) {
            tracker.logError("Traces for $trackingScreen have already been cleared!")
            return
        }

        activeTraces[trackingScreen] = null
    }
}