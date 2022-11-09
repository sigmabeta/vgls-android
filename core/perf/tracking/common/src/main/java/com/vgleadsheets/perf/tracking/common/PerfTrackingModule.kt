package com.vgleadsheets.perf.tracking.common

import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.perf.tracking.api.PerfTracker
import dagger.Module
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
