package com.vgleadsheets.analytics

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.sigmabeta.sage.analytics.Analytics
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NoopVglsAnalyticsModule {
    @Provides
    @Singleton
    fun provideNoopVglsAnalytics(): NoopVglsAnalytics = NoopVglsAnalytics()

    @Provides
    @Singleton
    fun provideVglsAnalytics(impl: NoopVglsAnalytics): VglsAnalytics = impl

    @Provides
    @Singleton
    fun provideAnalytics(impl: NoopVglsAnalytics): Analytics = impl
}
