package com.vgleadsheets.di

import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.perf.tracking.common.PerfTracker
import com.vgleadsheets.perf.tracking.common.PerfTrackerImpl
import com.vgleadsheets.perf.tracking.common.PerfTrackingBackend
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object PerfTrackingModule {
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
