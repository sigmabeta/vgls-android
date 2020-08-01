package com.vgleadsheets.perf.tracking.common

import com.vgleadsheets.perf.tracking.api.PerfStage

data class PerfScreenStatus(
    val startTime: Long,
    val stages: MutableList<Boolean> = PerfStage
        .values()
        .map { false }
        .toMutableList()
)
