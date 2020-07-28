package com.vgleadsheets.perf.view.common

data class PerfViewScreenStatus(
    val screenName: String,
    val targetTimes: Map<String, Long> = hashMapOf(),
    val startTime: Long = System.currentTimeMillis(),
    val durations: Map<String, Long> = hashMapOf(),
    val completionDuration: Long? = null,
    val cancellationDuration: Long? = null
)
