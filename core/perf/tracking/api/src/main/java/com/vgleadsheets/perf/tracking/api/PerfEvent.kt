package com.vgleadsheets.perf.tracking.api

data class PerfEvent(
    val screenName: String,
    val perfStage: PerfStage?,
    val duration: Long,
    val targetTimes: Map<String, Long>? = null
)
