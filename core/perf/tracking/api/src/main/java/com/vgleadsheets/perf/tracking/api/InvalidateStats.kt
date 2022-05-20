package com.vgleadsheets.perf.tracking.api

data class InvalidateStats(
    val jankInvalidates: Int,
    val totalInvalidates: Int,
    val median: Long,
    val ninetyFive: Long,
    val ninetyNine: Long
)
