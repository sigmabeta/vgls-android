package com.vgleadsheets.perf.tracking.impl

import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace
import com.vgleadsheets.perf.tracking.common.PerfStage
import com.vgleadsheets.perf.tracking.common.PerfTracker
import com.vgleadsheets.tracking.Tracker
import timber.log.Timber

class PerfTrackerImpl(
    private val firebase: FirebasePerformance,
    private val tracker: Tracker
) : PerfTracker {

    private val activeTraces =
        HashMap<String, MutableMap<PerfStage, Trace?>?>()

    override fun start(screenName: String) {
        // Check if an active trace list exists
        if (activeTraces[screenName] != null) {
            tracker.logError(
                "Active trace list from a previous instance of screen $screenName " +
                        "still exists!"
            )
        }

        // Create a new list of active traces
        val newTraces = HashMap<PerfStage, Trace?>(PerfStage.values().size)

        PerfStage.values().forEach { stage ->
            val newTrace = firebase.newTrace("$screenName:$stage")
            newTraces[stage] = newTrace
            newTrace.start()
        }

        // Add it to the hashmap
        activeTraces[screenName] = newTraces
    }

    override fun onViewCreated(screenName: String) =
        finishTrace(screenName, PerfStage.VIEW_CREATED)

    override fun onTitleLoaded(screenName: String) =
        finishTrace(screenName, PerfStage.TITLE_LOADED)

    override fun onTransitionStarted(screenName: String) =
        finishTrace(screenName, PerfStage.TRANSITION_START)

    override fun onPartialContentLoad(screenName: String) =
        finishTrace(screenName, PerfStage.PARTIAL_CONTENT_LOAD)

    override fun onFullContentLoad(screenName: String): Long? {
        val duration = finishTrace(screenName, PerfStage.FULL_CONTENT_LOAD) ?: return null

        removeTracesFor(screenName)

        return duration
    }

    override fun cancel(screenName: String) {
        removeTracesFor(screenName)
    }

    private fun finishTrace(screenName: String, perfStage: PerfStage): Long? {
        val tracesForScreen = activeTraces[screenName] ?: return null

        val trace = tracesForScreen[perfStage]

        if (trace == null) {
            val message = "Trace $screenName:$perfStage has already been stopped!"
            Timber.e(message)
            tracker.logError(message)
            return null
        }

        trace.stop()
        tracesForScreen[perfStage] = null

        return trace.getLongMetric("duration")
    }

    private fun removeTracesFor(screenName: String) {
        activeTraces[screenName] = null
    }
}