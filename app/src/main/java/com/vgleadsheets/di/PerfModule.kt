package com.vgleadsheets.di

import net.sigmabeta.sage.coroutines.VglsDispatchers
import net.sigmabeta.sage.perf.PerfBackend
import net.sigmabeta.sage.perf.PerfMeasurer
import net.sigmabeta.sage.perf.PerfMeasurerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object PerfModule {
    @Provides
    @Singleton
    fun providePerfMeasurer(
        backend: PerfBackend,
        dispatchers: VglsDispatchers
    ): PerfMeasurer {
        HashMap<String, Long>()
        return PerfMeasurerImpl(
            backend,
            dispatchers
        )
    }
}
