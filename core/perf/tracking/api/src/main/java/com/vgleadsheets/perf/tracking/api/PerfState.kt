package com.vgleadsheets.perf.tracking.api

data class PerfState(val screens: Map<PerfSpec, PerfScreenStatus>)
