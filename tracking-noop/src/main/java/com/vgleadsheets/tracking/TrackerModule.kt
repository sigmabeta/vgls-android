package com.vgleadsheets.tracking

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TrackerModule {
    @Provides
    @Singleton
    fun provideTracker(): Tracker = NoopTracker()
}
