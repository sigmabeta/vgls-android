package com.vgleadsheets.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.perf.PerfBackend
import net.sigmabeta.sage.perf.PerfMeasurer
import net.sigmabeta.sage.perf.PerfMeasurerImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object PerfModule {
    @Provides
    @Singleton
    fun providePerfMeasurer(
        backend: PerfBackend,
        dispatchers: SageDispatchers
    ): PerfMeasurer {
        HashMap<String, Long>()
        return PerfMeasurerImpl(
            backend,
            dispatchers
        )
    }
}
