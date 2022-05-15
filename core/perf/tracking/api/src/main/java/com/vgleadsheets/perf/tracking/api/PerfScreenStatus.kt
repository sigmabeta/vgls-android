package com.vgleadsheets.perf.tracking.api

import java.util.EnumMap

data class PerfScreenStatus(
    val name: String,
    val startTime: Long,
    val stageDurations: Map<PerfStage, Long?> = EnumMap(PerfStage::class.java)
)
