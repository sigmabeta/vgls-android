package com.vgleadsheets.perf.noop

import com.vgleadsheets.perf.common.PerfStage
import com.vgleadsheets.perf.common.PerfBackend

class NoopBackend : PerfBackend {
    override fun startScreen(screenName: String) = Unit

    override fun finishTrace(screenName: String, perfStage: PerfStage) = Unit

    override fun cancel(screenName: String) = Unit

    override fun error(message: String) {
        // TODO Re-enable after compose refactor is done
        // throw IllegalStateException(message)
    }
}
