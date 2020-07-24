package com.vgleadsheets.perf.view.common

interface PerfView {
    fun start(
        screenName: String,
        targetTimes: HashMap<String, Long>
    )
    fun completed(screenName: String)
    fun cancelled(screenName: String)
    fun viewCreated(screenName: String)
    fun titleLoaded(screenName: String)
    fun transitionStarted(screenName: String)
    fun partialContentLoaded(screenName: String)
    fun fullContentLoaded(screenName: String)
}