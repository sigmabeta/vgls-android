package com.vgleadsheets.perf.tracking

import com.vgleadsheets.perf.tracking.common.PerfTrackingBackend
import com.vgleadsheets.perf.tracking.noop.NoopTrackingBackend
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PerfBackendModule {
    @Provides
    @Singleton
    fun providePerfBackend(): PerfTrackingBackend = NoopTrackingBackend()
}