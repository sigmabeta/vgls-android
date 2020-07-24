package com.vgleadsheets.perf.tracking.common

import io.reactivex.Observable


interface PerfTracker {
    fun start(screenName: String)

    fun onViewCreated(screenName: String)

    fun onTitleLoaded(screenName: String)

    fun onTransitionStarted(screenName: String)

    fun onPartialContentLoad(screenName: String)

    fun onFullContentLoad(screenName: String)

    fun cancel(screenName: String)

    fun getEventStream(): Observable<PerfEvent>
}