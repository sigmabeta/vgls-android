package com.vgleadsheets.di

import com.vgleadsheets.logging.AndroidHatchet
import com.vgleadsheets.logging.BluntHatchet
import com.vgleadsheets.logging.Hatchet
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LoggingModule {
    @Provides
    @Singleton
    fun provideHatchet(): Hatchet = if (BuildConfig.DEBUG) {
        AndroidHatchet()
    } else {
        BluntHatchet()
    }
}
