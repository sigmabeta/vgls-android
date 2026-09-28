package com.vgleadsheets.analytics

import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.di.AppScope

@BindingContainer
@ContributesTo(AppScope::class)
object NoopVglsAnalyticsModule {
    @Provides
    @SingleIn(AppScope::class)
    fun provideNoopVglsAnalytics(): NoopVglsAnalytics = NoopVglsAnalytics()

    @Provides
    @SingleIn(AppScope::class)
    fun provideVglsAnalytics(impl: NoopVglsAnalytics): VglsAnalytics = impl

    @Provides
    @SingleIn(AppScope::class)
    fun provideAnalytics(impl: NoopVglsAnalytics): Analytics = impl
}
