package com.vgleadsheets.di

import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.perf.common.PerfBackend
import com.vgleadsheets.perf.common.PerfMeasurer
import com.vgleadsheets.perf.common.PerfMeasurerImpl
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
