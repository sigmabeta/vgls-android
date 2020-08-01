package com.vgleadsheets.perf.tracking.noop

import com.vgleadsheets.perf.tracking.api.PerfStage
import com.vgleadsheets.perf.tracking.common.PerfTrackingBackend

class NoopTrackingBackend : PerfTrackingBackend {
    private val screenStartTimes = hashMapOf<String, Long>()

    override fun startScreen(screenName: String): Long {
        val startTime = System.currentTimeMillis()
        screenStartTimes[screenName] = startTime
        return startTime
    }

    override fun finishTrace(screenName: String, perfStage: PerfStage): Long {
        val startTime = screenStartTimes[screenName]

        if (startTime == null) {
            error("Screen $screenName not found.")
            return -1L
        }

        return System.currentTimeMillis() - startTime
    }

    override fun cancel(screenName: String) = Unit

    override fun error(message: String) {
        throw IllegalStateException(message)
    }
}
