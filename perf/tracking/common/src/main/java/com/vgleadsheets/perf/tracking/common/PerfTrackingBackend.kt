package com.vgleadsheets.perf.tracking.common

import com.vgleadsheets.perf.tracking.api.PerfStage

interface PerfTrackingBackend {
    fun startScreen(screenName: String)
    fun finishTrace(screenName: String, perfStage: PerfStage)
    fun cancel(screenName: String)
    fun error(message: String)
}
