package com.vgleadsheets.perf.tracking

import com.google.firebase.perf.FirebasePerformance
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PerfTrackingModule {
    @Provides
    @Singleton
    fun provideFirebasePerfInstance(): FirebasePerformance = FirebasePerformance.getInstance()

}
