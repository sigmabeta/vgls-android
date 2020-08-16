package com.vgleadsheets.perf.tracking.noop

import com.vgleadsheets.perf.tracking.api.PerfStage
import com.vgleadsheets.perf.tracking.common.PerfTrackingBackend

class NoopTrackingBackend : PerfTrackingBackend {
    override fun startScreen(screenName: String) = Unit

    override fun finishTrace(screenName: String, perfStage: PerfStage) = Unit

    override fun cancel(screenName: String) = Unit

    override fun error(message: String) {
        throw RuntimeException(message)
    }
}
