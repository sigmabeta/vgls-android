package com.vgleadsheets.di

import com.google.firebase.perf.FirebasePerformance
import com.vgleadsheets.perf.tracking.common.PerfTrackingBackend
import com.vgleadsheets.perf.tracking.firebase.FirebasePerfBackend
import com.vgleadsheets.tracking.Tracker
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PerfBackendModule {
    @Provides
    @Singleton
    fun provideFirebasePerfInstance(): FirebasePerformance = FirebasePerformance.getInstance()

    @Provides
    @Singleton
    fun providePerfBackend(
        firebase: FirebasePerformance,
        tracker: Tracker
    ): PerfTrackingBackend = FirebasePerfBackend(
        firebase,
        tracker
    )
}
