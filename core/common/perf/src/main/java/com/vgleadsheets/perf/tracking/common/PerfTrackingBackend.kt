package com.vgleadsheets.perf.tracking.common

interface PerfTrackingBackend {
    fun startScreen(screenName: String)
    fun finishTrace(screenName: String, perfStage: PerfStage)
    fun cancel(screenName: String)
    fun error(message: String)
}
