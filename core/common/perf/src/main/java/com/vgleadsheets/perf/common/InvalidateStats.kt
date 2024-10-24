package com.vgleadsheets.perf.common

data class InvalidateStats(
    val jankInvalidates: Int,
    val totalInvalidates: Int,
    val totalInvalidateTimeMillis: Long,
    val medianMillis: Long,
    val ninetyFiveMillis: Long,
    val ninetyNineMillis: Long
)
