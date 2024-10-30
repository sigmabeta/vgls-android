package com.vgleadsheets.analytics

import com.vgleadsheets.logging.Hatchet
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AnalyticsModule {
    @Provides
    @Singleton
    fun provideAnalytics(hatchet: Hatchet): Analytics = NoopAnalytics(hatchet)
}
