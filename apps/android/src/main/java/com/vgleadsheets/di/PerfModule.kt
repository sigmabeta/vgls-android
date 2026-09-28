package com.vgleadsheets.di

import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.di.AppScope
import net.sigmabeta.sage.perf.PerfBackend
import net.sigmabeta.sage.perf.PerfMeasurer
import net.sigmabeta.sage.perf.PerfMeasurerImpl

@BindingContainer
@ContributesTo(AppScope::class)
object PerfModule {
    @Provides
    @SingleIn(AppScope::class)
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
