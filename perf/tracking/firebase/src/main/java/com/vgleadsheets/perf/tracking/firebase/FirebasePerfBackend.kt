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

    override fun startScreen(screenName: String) {
        screens[screenName] = PerfStage.values()
            .map {
                firebase.newTrace("$screenName:${it.name}")
            }

        screens[screenName]?.forEach { it.start() }
    }

    override fun finishTrace(screenName: String, perfStage: PerfStage) {
        val trace = screens[screenName]?.get(perfStage.ordinal)

        if (trace == null) {
            error("Could not find trace to finish: $screenName:$perfStage")
            return
        }

        trace.stop()
    }

    override fun cancel(screenName: String) {
        screens.remove(screenName)
    }

    override fun error(message: String) {
        tracker.logError("Perf error: $message")
    }
}
