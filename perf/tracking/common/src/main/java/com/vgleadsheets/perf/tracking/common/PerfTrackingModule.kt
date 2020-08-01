package com.vgleadsheets.perf.tracking.common

import com.vgleadsheets.perf.tracking.api.PerfTracker
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PerfTrackingModule {
    @Provides
    @Singleton
    fun providePerfTracker(backend: PerfTrackingBackend): PerfTracker = PerfTrackerImpl(backend)
}
