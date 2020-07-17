package com.vgleadsheets.perf.tracking

import com.google.firebase.perf.FirebasePerformance
import com.vgleadsheets.perf.tracking.common.PerfTracker
import com.vgleadsheets.perf.tracking.impl.PerfTrackerImpl
import com.vgleadsheets.tracking.Tracker
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PerfTrackingModule {
    @Provides
    @Singleton
    fun provideFirebasePerfInstance(): FirebasePerformance = FirebasePerformance.getInstance()

    @Provides
    @Singleton
    fun providePerfTracker(
        firebase: FirebasePerformance,
        tracker: Tracker
    ): PerfTracker = PerfTrackerImpl(
        firebase,
        tracker
    )
}
