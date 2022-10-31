package com.vgleadsheets.di

import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.perf.tracking.common.PerfTracker
import com.vgleadsheets.perf.tracking.common.PerfTrackerImpl
import com.vgleadsheets.perf.tracking.common.PerfTrackingBackend
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
