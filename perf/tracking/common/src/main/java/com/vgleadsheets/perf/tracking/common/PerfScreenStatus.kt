package com.vgleadsheets.perf.tracking.common

data class PerfScreenStatus(
    val startTime: Long = System.currentTimeMillis(),
    var cancelled: Boolean = false,
    var completed: Boolean = false,
    val stages: MutableList<Boolean> = PerfStage
        .values()
        .map { false }
        .toMutableList()
)