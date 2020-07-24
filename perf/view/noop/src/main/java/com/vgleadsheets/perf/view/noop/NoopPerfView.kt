package com.vgleadsheets.perf.view.noop

import com.vgleadsheets.perf.view.common.PerfView

class NoopPerfView : PerfView {

    override fun start(screenName: String, targetTimes: HashMap<String, Long>) = Unit

    override fun completed(screenName: String) = Unit

    override fun cancelled(screenName: String) = Unit

    override fun viewCreated(screenName: String) = Unit

    override fun titleLoaded(screenName: String) = Unit

    override fun transitionStarted(screenName: String) = Unit

    override fun partialContentLoaded(screenName: String) = Unit

    override fun fullContentLoaded(screenName: String) = Unit
}