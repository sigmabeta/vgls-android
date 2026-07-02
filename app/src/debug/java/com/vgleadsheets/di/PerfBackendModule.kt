package com.vgleadsheets.di

import net.sigmabeta.sage.perf.PerfBackend
import net.sigmabeta.sage.perf.NoopBackend
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import net.sigmabeta.sage.di.AppScope

@BindingContainer
@ContributesTo(AppScope::class)
object PerfBackendModule {
    @Provides
    @SingleIn(AppScope::class)
    fun providePerfBackend(): PerfBackend = NoopBackend()
}
