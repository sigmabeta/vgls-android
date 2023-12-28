package com.vgleadsheets.di

import com.vgleadsheets.logging.AndroidHatchet
import com.vgleadsheets.logging.BluntHatchet
import com.vgleadsheets.logging.BuildConfig
import com.vgleadsheets.logging.Hatchet
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object LoggingModule {
    @Provides
    @Singleton
    fun provideHatchet(): Hatchet = if (BuildConfig.DEBUG) {
        AndroidHatchet()
    } else {
        BluntHatchet()
    }
}
