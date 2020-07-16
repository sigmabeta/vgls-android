package com.vgleadsheets.perf.tracking

import com.vgleadsheets.perf.tracking.common.PerfTracker
import com.vgleadsheets.perf.tracking.impl.PerfTrackerImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PerfTrackingModule {
    @Provides
    @Singleton
    fun providePerfTracker(): PerfTracker = PerfTrackerImpl()
}
