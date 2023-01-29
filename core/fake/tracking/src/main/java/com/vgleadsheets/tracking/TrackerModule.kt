package com.vgleadsheets.tracking

import com.vgleadsheets.logging.Hatchet
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TrackerModule {
    @Provides
    @Singleton
    fun provideTracker(hatchet: Hatchet): Tracker = NoopTracker(hatchet)
}
