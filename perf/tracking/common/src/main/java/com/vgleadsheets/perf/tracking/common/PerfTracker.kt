package com.vgleadsheets.perf.tracking.common

interface PerfTracker {
    fun start(screenName: String)

    fun onViewCreated(screenName: String): Long?

    fun onTitleLoaded(screenName: String): Long?

    fun onTransitionStarted(screenName: String): Long?

    fun onPartialContentLoad(screenName: String): Long?

    fun onFullContentLoad(screenName: String): Long?

    fun cancel(screenName: String)
}