package com.vgleadsheets.di

import com.vgleadsheets.perf.common.PerfBackend
import com.vgleadsheets.perf.noop.NoopBackend
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
    fun providePerfBackend(): PerfBackend = NoopBackend()
}
