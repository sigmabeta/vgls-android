package com.vgleadsheets.di

import com.vgleadsheets.perf.tracking.common.PerfTrackingBackend
import com.vgleadsheets.perf.tracking.noop.NoopTrackingBackend
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object PerfBackendModule {
    @Provides
    @Singleton
    fun providePerfBackend(): PerfTrackingBackend = NoopTrackingBackend()
}
