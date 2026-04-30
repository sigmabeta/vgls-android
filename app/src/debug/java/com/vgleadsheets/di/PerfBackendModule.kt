package com.vgleadsheets.di

import net.sigmabeta.sage.perf.PerfBackend
import net.sigmabeta.sage.perf.NoopBackend
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
