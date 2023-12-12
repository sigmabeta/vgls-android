package com.vgleadsheets.nav

import com.vgleadsheets.perf.tracking.common.PerfSpec

data class PerfViewState(
    val viewMode: PerfViewMode = PerfViewMode.REGULAR,
    val selectedScreen: PerfSpec = PerfSpec.values().first(),
)
