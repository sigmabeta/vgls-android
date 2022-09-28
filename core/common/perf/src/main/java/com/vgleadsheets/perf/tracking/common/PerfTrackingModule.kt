package com.vgleadsheets.perf.tracking.common

import dagger.Provides
import javax.inject.Singleton

@Module
class PerfTrackingModule {
    @Provides
    @Singleton
    fun providePerfTracker(
        backend: PerfTrackingBackend,
        dispatchers: VglsDispatchers
    ): PerfTracker = PerfTrackerImpl(
        backend,
        dispatchers
    )
}
