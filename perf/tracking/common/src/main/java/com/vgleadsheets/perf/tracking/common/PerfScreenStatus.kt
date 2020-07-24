package com.vgleadsheets.perf.tracking.common

data class PerfScreenStatus(
    val startTime: Long = System.currentTimeMillis(),
    val stages: MutableList<Boolean> = PerfStage
        .values()
        .map { false }
        .toMutableList()
)