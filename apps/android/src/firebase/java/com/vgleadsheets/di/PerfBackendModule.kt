package com.vgleadsheets.di

import com.google.firebase.perf.FirebasePerformance
import net.sigmabeta.sage.perf.PerfBackend
import net.sigmabeta.sage.perf.firebase.FirebasePerfBackend
import net.sigmabeta.sage.analytics.Analytics
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
    fun provideFirebasePerfInstance(): FirebasePerformance = FirebasePerformance.getInstance()

    @Provides
    @SingleIn(AppScope::class)
    fun providePerfBackend(
        firebase: FirebasePerformance,
        analytics: Analytics
    ): PerfBackend = FirebasePerfBackend(
        firebase,
        analytics
    )
}
