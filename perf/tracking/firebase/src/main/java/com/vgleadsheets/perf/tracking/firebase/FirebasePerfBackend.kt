package com.vgleadsheets.perf.tracking.firebase

import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace
import com.vgleadsheets.perf.tracking.api.PerfStage
import com.vgleadsheets.perf.tracking.common.PerfTrackingBackend
import com.vgleadsheets.tracking.Tracker

class FirebasePerfBackend(
    private val firebase: FirebasePerformance,
    private val tracker: Tracker
) : PerfTrackingBackend {
    private val screens = HashMap<String, List<Trace>>()

    override fun startScreen(screenName: String): Long {
        screens[screenName] = PerfStage.values()
            .map {
                firebase.newTrace("$screenName:${it.name}")
            }

        return System.currentTimeMillis()
    }

    override fun finishTrace(screenName: String, perfStage: PerfStage): Long {
        val trace = screens[screenName]?.get(perfStage.ordinal) ?: return -1L

        trace.stop()
        return trace.getLongMetric("Duration")
    }

    override fun cancel(screenName: String) {
        screens.remove(screenName)
    }

    override fun error(message: String) {
        tracker.logError("Perf error: $message")
    }
}
