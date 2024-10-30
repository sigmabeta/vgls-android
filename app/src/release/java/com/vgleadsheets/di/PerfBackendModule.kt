package com.vgleadsheets.di

import com.google.firebase.perf.FirebasePerformance
import com.vgleadsheets.perf.common.PerfBackend
import com.vgleadsheets.perf.firebase.FirebasePerfBackend
import com.vgleadsheets.analytics.Analytics
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
    fun provideFirebasePerfInstance(): FirebasePerformance = FirebasePerformance.getInstance()

    @Provides
    @Singleton
    fun providePerfBackend(
        firebase: FirebasePerformance,
        analytics: Analytics
    ): PerfBackend = FirebasePerfBackend(
        firebase,
        analytics
    )
}
