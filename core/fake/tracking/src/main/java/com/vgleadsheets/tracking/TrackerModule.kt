package com.vgleadsheets.tracking

import com.vgleadsheets.logging.Hatchet
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object TrackerModule {
    @Provides
    @Singleton
    fun provideTracker(hatchet: Hatchet): Tracker = NoopTracker(hatchet)
}
