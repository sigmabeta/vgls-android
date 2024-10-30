package com.vgleadsheets.perf.common

interface PerfBackend {
    fun startScreen(screenName: String)
    fun finishTrace(screenName: String, perfStage: PerfStage)
    fun cancel(screenName: String)
    fun error(message: String)
}
