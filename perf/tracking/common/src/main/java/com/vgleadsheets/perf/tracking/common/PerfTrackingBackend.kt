package com.vgleadsheets.perf.tracking.common

import com.vgleadsheets.perf.tracking.api.PerfStage

interface PerfTrackingBackend {
    fun startScreen(screenName: String): Long
    fun finishTrace(screenName: String, perfStage: PerfStage): Long
    fun cancel(screenName: String)
    fun error(message: String)
}